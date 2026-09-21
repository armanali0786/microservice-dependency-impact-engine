package com.dependencyimpact.impactanalysis.dto;

import java.util.List;
import java.util.UUID;

public class ImpactReportResponse {

    private final UUID analysisId;
    private final String sourceService;
    private final String riskLevel;
    private final ImpactSummary summary;
    private final List<ImpactComponentResponse> components;

    public ImpactReportResponse(UUID analysisId, String sourceService, String riskLevel,
                                 ImpactSummary summary, List<ImpactComponentResponse> components) {
        this.analysisId = analysisId;
        this.sourceService = sourceService;
        this.riskLevel = riskLevel;
        this.summary = summary;
        this.components = components;
    }

    public UUID getAnalysisId() {
        return analysisId;
    }

    public String getSourceService() {
        return sourceService;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public ImpactSummary getSummary() {
        return summary;
    }

    public List<ImpactComponentResponse> getComponents() {
        return components;
    }
}
