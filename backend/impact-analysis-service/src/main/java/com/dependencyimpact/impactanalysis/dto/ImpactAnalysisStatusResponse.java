package com.dependencyimpact.impactanalysis.dto;

import java.time.Instant;
import java.util.UUID;

public class ImpactAnalysisStatusResponse {

    private final UUID id;
    private final String status;
    private final String riskLevel;
    private final Instant startedAt;
    private final Instant completedAt;
    private final String errorMessage;

    public ImpactAnalysisStatusResponse(UUID id, String status, String riskLevel,
                                         Instant startedAt, Instant completedAt, String errorMessage) {
        this.id = id;
        this.status = status;
        this.riskLevel = riskLevel;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.errorMessage = errorMessage;
    }

    public UUID getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
