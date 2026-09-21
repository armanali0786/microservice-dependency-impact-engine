package com.dependencyimpact.impactanalysis.dto;

import java.util.UUID;

// affectedApis is always 0 for now - per-API impact detection would need each
// downstream service's APIs correlated in, which isn't wired up yet. Flagged
// rather than faked, same as ImpactSummary.affectedTeams.
public record ImpactAnalysisCompletedPayload(
        UUID analysisId,
        String riskLevel,
        int affectedServices,
        int affectedApis,
        int affectedKafkaTopics
) {
}
