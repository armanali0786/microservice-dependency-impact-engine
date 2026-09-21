package com.dependencyimpact.runtimeanalysis.client;

import java.util.List;

public record ImpactComponentDto(String service, List<String> dependencyPath) {
}
