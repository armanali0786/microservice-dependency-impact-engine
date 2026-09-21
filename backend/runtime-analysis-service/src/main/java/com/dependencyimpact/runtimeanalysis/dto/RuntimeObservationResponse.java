package com.dependencyimpact.runtimeanalysis.dto;

import java.time.Instant;
import java.util.UUID;

public class RuntimeObservationResponse {

    private final UUID id;
    private final String traceId;
    private final String spanId;
    private final UUID sourceServiceId;
    private final UUID targetServiceId;
    private final String endpoint;
    private final String httpMethod;
    private final Integer statusCode;
    private final Double latencyMs;
    private final Instant observedAt;
    private final String environment;

    public RuntimeObservationResponse(UUID id, String traceId, String spanId, UUID sourceServiceId,
                                       UUID targetServiceId, String endpoint, String httpMethod,
                                       Integer statusCode, Double latencyMs, Instant observedAt, String environment) {
        this.id = id;
        this.traceId = traceId;
        this.spanId = spanId;
        this.sourceServiceId = sourceServiceId;
        this.targetServiceId = targetServiceId;
        this.endpoint = endpoint;
        this.httpMethod = httpMethod;
        this.statusCode = statusCode;
        this.latencyMs = latencyMs;
        this.observedAt = observedAt;
        this.environment = environment;
    }

    public UUID getId() {
        return id;
    }

    public String getTraceId() {
        return traceId;
    }

    public String getSpanId() {
        return spanId;
    }

    public UUID getSourceServiceId() {
        return sourceServiceId;
    }

    public UUID getTargetServiceId() {
        return targetServiceId;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public Double getLatencyMs() {
        return latencyMs;
    }

    public Instant getObservedAt() {
        return observedAt;
    }

    public String getEnvironment() {
        return environment;
    }
}
