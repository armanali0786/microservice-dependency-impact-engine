package com.dependencyimpact.impactanalysis.service;

import com.dependencyimpact.impactanalysis.dto.ImpactAnalysisRequest;
import com.dependencyimpact.impactanalysis.dto.ImpactAnalysisRequestedPayload;
import com.dependencyimpact.impactanalysis.dto.ImpactAnalysisResponse;
import com.dependencyimpact.impactanalysis.dto.ImpactAnalysisStatusResponse;
import com.dependencyimpact.impactanalysis.dto.ImpactComponentResponse;
import com.dependencyimpact.impactanalysis.dto.ImpactReportResponse;
import com.dependencyimpact.impactanalysis.dto.ImpactSummary;
import com.dependencyimpact.impactanalysis.entity.ImpactAnalysis;
import com.dependencyimpact.impactanalysis.entity.ImpactComponent;
import com.dependencyimpact.common.exceptions.ResourceNotFoundException;
import com.dependencyimpact.impactanalysis.entity.ServiceLookup;
import com.dependencyimpact.impactanalysis.exception.ImpactAnalysisException;
import com.dependencyimpact.impactanalysis.exception.ImpactAnalysisNotFoundException;
import com.dependencyimpact.impactanalysis.kafka.producer.ImpactAnalysisEventProducer;
import com.dependencyimpact.impactanalysis.mapper.ImpactAnalysisMapper;
import com.dependencyimpact.impactanalysis.mapper.ImpactComponentMapper;
import com.dependencyimpact.impactanalysis.repository.ImpactAnalysisRepository;
import com.dependencyimpact.impactanalysis.repository.ImpactComponentRepository;
import com.dependencyimpact.impactanalysis.repository.ServiceLookupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ImpactAnalysisService {

    // Same default/max as dependency-graph-service's bounded traversal, for consistency.
    private static final int DEFAULT_DEPTH = 2;
    private static final int MAX_DEPTH = 5;

    private final ImpactAnalysisRepository impactAnalysisRepository;
    private final ImpactComponentRepository impactComponentRepository;
    private final ServiceLookupRepository serviceLookupRepository;
    private final ImpactAnalysisMapper impactAnalysisMapper;
    private final ImpactComponentMapper impactComponentMapper;
    private final ImpactAnalysisEventProducer eventProducer;

    public ImpactAnalysisService(ImpactAnalysisRepository impactAnalysisRepository,
                                  ImpactComponentRepository impactComponentRepository,
                                  ServiceLookupRepository serviceLookupRepository,
                                  ImpactAnalysisMapper impactAnalysisMapper,
                                  ImpactComponentMapper impactComponentMapper,
                                  ImpactAnalysisEventProducer eventProducer) {
        this.impactAnalysisRepository = impactAnalysisRepository;
        this.impactComponentRepository = impactComponentRepository;
        this.serviceLookupRepository = serviceLookupRepository;
        this.impactAnalysisMapper = impactAnalysisMapper;
        this.impactComponentMapper = impactComponentMapper;
        this.eventProducer = eventProducer;
    }

    public ImpactAnalysisResponse startAnalysis(ImpactAnalysisRequest request) {
        // impact_analyses.source_service_id has a FK to services(id) - without this
        // check, an unknown serviceId reaches the database as a raw foreign-key
        // violation (500) instead of a clean, expected 404.
        if (!serviceLookupRepository.existsById(request.getServiceId())) {
            throw new ResourceNotFoundException("Service not found: " + request.getServiceId());
        }

        int depth = resolveDepth(request.getTraversalDepth());
        UUID id = UUID.randomUUID();

        ImpactAnalysis analysis = impactAnalysisMapper.newPending(request, id, depth);
        impactAnalysisRepository.save(analysis);

        eventProducer.publishRequested(
                new ImpactAnalysisRequestedPayload(id, request.getServiceId(), request.getChangeType(), depth),
                request.getEnvironment());

        return impactAnalysisMapper.toAnalysisResponse(analysis);
    }

    public ImpactAnalysisStatusResponse getStatus(UUID id) {
        return impactAnalysisMapper.toStatusResponse(findOrThrow(id));
    }

    public ImpactReportResponse getReport(UUID id) {
        ImpactAnalysis analysis = findOrThrow(id);
        if (!"COMPLETED".equals(analysis.getStatus())) {
            throw new ImpactAnalysisException(
                    "Analysis " + id + " is still " + analysis.getStatus() + " - report isn't ready yet");
        }

        List<ImpactComponent> components = impactComponentRepository.findByImpactAnalysisId(id);
        List<ImpactComponentResponse> responses = components.stream()
                .map(impactComponentMapper::toResponse)
                .toList();

        String sourceServiceName = serviceLookupRepository.findById(analysis.getSourceServiceId())
                .map(ServiceLookup::getName)
                .orElse(analysis.getSourceServiceId().toString());

        long direct = components.stream().filter(c -> Integer.valueOf(1).equals(c.getDepth())).count();
        long indirect = components.size() - direct;
        long kafkaTopics = components.stream().filter(c -> "KAFKA".equals(c.getDependencyType())).count();

        ImpactSummary summary = new ImpactSummary((int) direct, (int) indirect, (int) kafkaTopics);

        return new ImpactReportResponse(id, sourceServiceName, analysis.getRiskLevel(), summary, responses);
    }

    public List<ImpactComponentResponse> listComponents(UUID id, String impactLevel, String dependencyType, Integer depth) {
        findOrThrow(id);

        return impactComponentRepository.findByImpactAnalysisId(id).stream()
                .filter(c -> impactLevel == null || impactLevel.equalsIgnoreCase(c.getImpactLevel()))
                .filter(c -> dependencyType == null || dependencyType.equalsIgnoreCase(c.getDependencyType()))
                .filter(c -> depth == null || depth.equals(c.getDepth()))
                .map(impactComponentMapper::toResponse)
                .toList();
    }

    private ImpactAnalysis findOrThrow(UUID id) {
        return impactAnalysisRepository.findById(id)
                .orElseThrow(() -> new ImpactAnalysisNotFoundException("Impact analysis not found: " + id));
    }

    private int resolveDepth(Integer requestedDepth) {
        int depth = requestedDepth != null ? requestedDepth : DEFAULT_DEPTH;
        if (depth < 1 || depth > MAX_DEPTH) {
            throw new ImpactAnalysisException(
                    "Requested traversal depth " + depth + " exceeds the allowed range (1-" + MAX_DEPTH + ")");
        }
        return depth;
    }
}
