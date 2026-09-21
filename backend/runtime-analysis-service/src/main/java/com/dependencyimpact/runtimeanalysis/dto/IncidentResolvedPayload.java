package com.dependencyimpact.runtimeanalysis.dto;

import java.time.Instant;
import java.util.UUID;

public record IncidentResolvedPayload(UUID incidentId, UUID serviceId, Instant resolvedAt) {
}
