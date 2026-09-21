package com.dependencyimpact.common.events;

public record EventEnvelope<T>(String eventId, EventType eventType, int schemaVersion, String producer, String environment, java.time.Instant timestamp, String correlationId, String traceId, T payload) {
}
