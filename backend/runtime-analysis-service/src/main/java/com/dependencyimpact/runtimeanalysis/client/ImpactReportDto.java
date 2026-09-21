package com.dependencyimpact.runtimeanalysis.client;

import java.util.List;
import java.util.UUID;

// Only the fields IncidentService needs to build an IncidentImpactResponse - not a
// full mirror of impact-analysis-service's ImpactReportResponse. Jackson ignores the
// extra fields it doesn't ask for (Spring Boot's default ObjectMapper doesn't fail
// on unknown properties).
public record ImpactReportDto(UUID analysisId, String sourceService, List<ImpactComponentDto> components) {
}
