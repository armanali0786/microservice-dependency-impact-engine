package com.dependencyimpact.impactanalysis.dto;

import java.util.UUID;

public record ImpactAnalysisRequestedPayload(UUID analysisId, UUID rootServiceId, String changeType, int maxDepth) {
}
