package com.dependencyimpact.runtimeanalysis.dto;

import java.util.UUID;

public record RuntimeObservationReceivedPayload(
        UUID sourceServiceId,
        UUID targetServiceId,
        Double latencyMs,
        Integer statusCode,
        String environment
) {
}
