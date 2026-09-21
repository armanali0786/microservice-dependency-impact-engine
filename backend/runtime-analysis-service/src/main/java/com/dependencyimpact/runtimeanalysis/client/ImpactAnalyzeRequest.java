package com.dependencyimpact.runtimeanalysis.client;

import java.util.UUID;

public record ImpactAnalyzeRequest(UUID serviceId, String changeType, String environment) {
}
