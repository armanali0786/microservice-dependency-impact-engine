package com.dependencyimpact.impactanalysis.dto;

import java.util.UUID;

public class ImpactAnalysisResponse {

    private final UUID analysisId;
    private final String status;

    public ImpactAnalysisResponse(UUID analysisId, String status) {
        this.analysisId = analysisId;
        this.status = status;
    }

    public UUID getAnalysisId() {
        return analysisId;
    }

    public String getStatus() {
        return status;
    }
}
