package com.dependencyimpact.dependencycollector.dto;

import jakarta.validation.constraints.NotBlank;

public class ManualDependencyObservationRequest {

    @NotBlank(message = "sourceService is required")
    private String sourceService;

    @NotBlank(message = "targetService is required")
    private String targetService;

    // One of: REST, KAFKA, DATABASE, CACHE, QUEUE, EXTERNAL_API, RUNTIME
    @NotBlank(message = "dependencyType is required")
    private String dependencyType;

    private String protocol;
    private String endpoint;

    @NotBlank(message = "environment is required")
    private String environment;

    // One of: STATIC, RUNTIME, BOTH, UNKNOWN - defaults to STATIC if omitted
    private String confidence;

    public String getSourceService() {
        return sourceService;
    }

    public void setSourceService(String sourceService) {
        this.sourceService = sourceService;
    }

    public String getTargetService() {
        return targetService;
    }

    public void setTargetService(String targetService) {
        this.targetService = targetService;
    }

    public String getDependencyType() {
        return dependencyType;
    }

    public void setDependencyType(String dependencyType) {
        this.dependencyType = dependencyType;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }
}
