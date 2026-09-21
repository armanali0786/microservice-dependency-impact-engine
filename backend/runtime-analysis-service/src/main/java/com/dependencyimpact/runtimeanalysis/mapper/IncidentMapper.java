package com.dependencyimpact.runtimeanalysis.mapper;

import com.dependencyimpact.runtimeanalysis.dto.CreateIncidentRequest;
import com.dependencyimpact.runtimeanalysis.dto.IncidentResponse;
import com.dependencyimpact.runtimeanalysis.entity.Incident;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class IncidentMapper {

    public Incident toEntity(CreateIncidentRequest request) {
        Incident incident = new Incident();
        incident.setId(UUID.randomUUID());
        incident.setServiceId(request.getServiceId());
        incident.setTitle(request.getTitle());
        incident.setDescription(request.getDescription());
        incident.setSeverity(request.getSeverity());
        incident.setStatus("OPEN");
        incident.setStartedAt(Instant.now());
        incident.setCreatedAt(Instant.now());
        incident.setUpdatedAt(Instant.now());
        return incident;
    }

    public Incident newAutoDetected(UUID serviceId, String title, String description, String severity,
                                     Double errorRate, Double latencyMs) {
        Incident incident = new Incident();
        incident.setId(UUID.randomUUID());
        incident.setServiceId(serviceId);
        incident.setTitle(title);
        incident.setDescription(description);
        incident.setSeverity(severity);
        incident.setStatus("OPEN");
        incident.setErrorRate(errorRate);
        incident.setLatencyMs(latencyMs);
        incident.setStartedAt(Instant.now());
        incident.setCreatedAt(Instant.now());
        incident.setUpdatedAt(Instant.now());
        return incident;
    }

    public IncidentResponse toResponse(Incident incident) {
        return new IncidentResponse(
                incident.getId(),
                incident.getServiceId(),
                incident.getTitle(),
                incident.getDescription(),
                incident.getSeverity(),
                incident.getStatus(),
                incident.getErrorRate(),
                incident.getLatencyMs(),
                incident.getKafkaLag(),
                incident.getStartedAt(),
                incident.getResolvedAt(),
                incident.getImpactAnalysisId()
        );
    }
}
