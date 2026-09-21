package com.dependencyimpact.common.model;

public record DependencyObservation(String sourceServiceId, String targetServiceId, DependencyType type, String protocol, String endpoint, String topic, String environment, java.time.Instant observedAt, DependencyConfidence confidence, java.util.Map<String, Object> metadata) {
}
