package com.dependencyimpact.runtimeanalysis.client;

import java.util.UUID;

// Mirrors impact-analysis-service's ImpactAnalysisResponse - only the fields this
// client actually needs from that contract (see GraphServiceClient in
// impact-analysis-service for the reasoning on duplicating cross-service DTOs).
public record ImpactAnalyzeResponse(UUID analysisId, String status) {
}
