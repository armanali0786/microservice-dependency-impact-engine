package com.dependencyimpact.runtimeanalysis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public class RuntimeObservationRequest {

    private String traceId;
    private String spanId;

    @NotNull
    private UUID sourceServiceId;

    @NotNull
    private UUID targetServiceId;

    private String endpoint;
    private String httpMethod;
    private Integer statusCode;

    @NotNull
    private Double latencyMs;

    @NotBlank
    private String environment;

    private Instant observedAt;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public UUID getSourceServiceId() {
        return sourceServiceId;
    }

    public void setSourceServiceId(UUID sourceServiceId) {
        this.sourceServiceId = sourceServiceId;
    }

    public UUID getTargetServiceId() {
        return targetServiceId;
    }

    public void setTargetServiceId(UUID targetServiceId) {
        this.targetServiceId = targetServiceId;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Double getLatencyMs() {
        return latencyMs;
    }

    public void setLatencyMs(Double latencyMs) {
        this.latencyMs = latencyMs;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public Instant getObservedAt() {
        return observedAt;
    }

    public void setObservedAt(Instant observedAt) {
        this.observedAt = observedAt;
    }
}
