package com.dependencyimpact.impactanalysis.dto;

import java.util.UUID;

public record ImpactAnalysisFailedPayload(UUID analysisId, String errorMessage) {
}
