package com.dependencyimpact.runtimeanalysis.dto;

import java.util.UUID;

public record IncidentCreatedPayload(UUID incidentId, UUID serviceId, String severity, String title) {
}
