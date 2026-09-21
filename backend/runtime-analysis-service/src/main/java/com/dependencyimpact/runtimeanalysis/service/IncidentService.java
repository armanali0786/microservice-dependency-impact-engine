package com.dependencyimpact.runtimeanalysis.service;

import com.dependencyimpact.common.exceptions.ResourceNotFoundException;
import com.dependencyimpact.runtimeanalysis.client.ImpactAnalysisClient;
import com.dependencyimpact.runtimeanalysis.client.ImpactAnalyzeResponse;
import com.dependencyimpact.runtimeanalysis.client.ImpactComponentDto;
import com.dependencyimpact.runtimeanalysis.client.ImpactReportDto;
import com.dependencyimpact.runtimeanalysis.dto.CreateIncidentRequest;
import com.dependencyimpact.runtimeanalysis.dto.IncidentCreatedPayload;
import com.dependencyimpact.runtimeanalysis.dto.IncidentImpactResponse;
import com.dependencyimpact.runtimeanalysis.dto.IncidentResolvedPayload;
import com.dependencyimpact.runtimeanalysis.dto.IncidentResponse;
import com.dependencyimpact.runtimeanalysis.entity.Incident;
import com.dependencyimpact.runtimeanalysis.exception.IncidentNotFoundException;
import com.dependencyimpact.runtimeanalysis.exception.RuntimeAnalysisException;
import com.dependencyimpact.runtimeanalysis.kafka.producer.IncidentEventProducer;
import com.dependencyimpact.runtimeanalysis.mapper.IncidentMapper;
import com.dependencyimpact.runtimeanalysis.repository.IncidentRepository;
import com.dependencyimpact.runtimeanalysis.repository.ServiceLookupRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final ServiceLookupRepository serviceLookupRepository;
    private final IncidentMapper incidentMapper;
    private final IncidentEventProducer incidentEventProducer;
    private final ImpactAnalysisClient impactAnalysisClient;

    public IncidentService(IncidentRepository incidentRepository, ServiceLookupRepository serviceLookupRepository,
                            IncidentMapper incidentMapper, IncidentEventProducer incidentEventProducer,
                            ImpactAnalysisClient impactAnalysisClient) {
        this.incidentRepository = incidentRepository;
        this.serviceLookupRepository = serviceLookupRepository;
        this.incidentMapper = incidentMapper;
        this.incidentEventProducer = incidentEventProducer;
        this.impactAnalysisClient = impactAnalysisClient;
    }

    public IncidentResponse createIncident(CreateIncidentRequest request) {
        if (!serviceLookupRepository.existsById(request.getServiceId())) {
            throw new ResourceNotFoundException("Service not found: " + request.getServiceId());
        }

        Incident incident = incidentMapper.toEntity(request);
        incidentRepository.save(incident);

        incidentEventProducer.publishCreated(new IncidentCreatedPayload(
                incident.getId(), incident.getServiceId(), incident.getSeverity(), incident.getTitle()));

        return incidentMapper.toResponse(incident);
    }

    public List<IncidentResponse> listIncidents(UUID serviceId, String severity, String status,
                                                 Instant from, Instant to) {
        List<Incident> incidents = incidentRepository.findAll();

        return incidents.stream()
                .filter(i -> serviceId == null || serviceId.equals(i.getServiceId()))
                .filter(i -> severity == null || severity.equalsIgnoreCase(i.getSeverity()))
                .filter(i -> status == null || status.equalsIgnoreCase(i.getStatus()))
                .filter(i -> from == null || !i.getStartedAt().isBefore(from))
                .filter(i -> to == null || !i.getStartedAt().isAfter(to))
                .map(incidentMapper::toResponse)
                .toList();
    }

    public IncidentResponse getIncident(UUID id) {
        return incidentMapper.toResponse(findOrThrow(id));
    }

    public IncidentResponse updateStatus(UUID id, String status) {
        Incident incident = findOrThrow(id);
        incident.setStatus(status);
        incident.setUpdatedAt(Instant.now());
        if ("RESOLVED".equals(status) && incident.getResolvedAt() == null) {
            incident.setResolvedAt(Instant.now());
        }
        incidentRepository.save(incident);

        if ("RESOLVED".equals(status)) {
            incidentEventProducer.publishResolved(
                    new IncidentResolvedPayload(incident.getId(), incident.getServiceId(), incident.getResolvedAt()));
        }

        return incidentMapper.toResponse(incident);
    }

    // The incidents table has no environment column (docs/database-design.md's
    // schema doesn't carry one, and POST /incidents doesn't ask for one either),
    // but impact-analysis-service's POST /impact/analyze requires one. Defaulting
    // to "production" here is a judgment call, not a spec'd value - incidents are
    // overwhelmingly a production concern in practice.
    public ImpactAnalyzeResponse triggerImpactAnalysis(UUID id, String environment) {
        Incident incident = findOrThrow(id);
        String resolvedEnvironment = environment != null ? environment : "production";

        ImpactAnalyzeResponse response = impactAnalysisClient.startAnalysis(
                incident.getServiceId(), "FAILURE", resolvedEnvironment);

        incident.setImpactAnalysisId(response.analysisId());
        incident.setUpdatedAt(Instant.now());
        incidentRepository.save(incident);

        return response;
    }

    public IncidentImpactResponse getImpact(UUID id) {
        Incident incident = findOrThrow(id);
        if (incident.getImpactAnalysisId() == null) {
            throw new RuntimeAnalysisException(
                    "No impact analysis has been triggered for incident " + id
                            + " yet - call POST /api/v1/incidents/" + id + "/analyze-impact first");
        }

        ImpactReportDto report = impactAnalysisClient.getReport(incident.getImpactAnalysisId());
        if (report == null) {
            throw new RuntimeAnalysisException(
                    "Impact analysis for incident " + id + " is still running - try again shortly");
        }

        List<String> affectedServices = report.components().stream()
                .map(ImpactComponentDto::service)
                .toList();
        List<List<String>> dependencyPaths = report.components().stream()
                .map(ImpactComponentDto::dependencyPath)
                .toList();

        return new IncidentImpactResponse(id, affectedServices, dependencyPaths);
    }

    private Incident findOrThrow(UUID id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new IncidentNotFoundException("Incident not found: " + id));
    }
}
