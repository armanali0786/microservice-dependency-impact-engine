package com.dependencyimpact.runtimeanalysis.dto;

import java.time.Instant;
import java.util.UUID;

public class IncidentResponse {

    private final UUID id;
    private final UUID serviceId;
    private final String title;
    private final String description;
    private final String severity;
    private final String status;
    private final Double errorRate;
    private final Double latencyMs;
    private final Long kafkaLag;
    private final Instant startedAt;
    private final Instant resolvedAt;
    private final UUID impactAnalysisId;

    public IncidentResponse(UUID id, UUID serviceId, String title, String description, String severity,
                             String status, Double errorRate, Double latencyMs, Long kafkaLag,
                             Instant startedAt, Instant resolvedAt, UUID impactAnalysisId) {
        this.id = id;
        this.serviceId = serviceId;
        this.title = title;
        this.description = description;
        this.severity = severity;
        this.status = status;
        this.errorRate = errorRate;
        this.latencyMs = latencyMs;
        this.kafkaLag = kafkaLag;
        this.startedAt = startedAt;
        this.resolvedAt = resolvedAt;
        this.impactAnalysisId = impactAnalysisId;
    }

    public UUID getId() {
        return id;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSeverity() {
        return severity;
    }

    public String getStatus() {
        return status;
    }

    public Double getErrorRate() {
        return errorRate;
    }

    public Double getLatencyMs() {
        return latencyMs;
    }

    public Long getKafkaLag() {
        return kafkaLag;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getResolvedAt() {
        return resolvedAt;
    }

    public UUID getImpactAnalysisId() {
        return impactAnalysisId;
    }
}
