package com.dependencyimpact.impactanalysis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class ImpactAnalysisRequest {

    @NotNull
    private UUID serviceId;

    @NotBlank
    private String changeType;

    @NotBlank
    private String environment;

    private UUID apiId;

    private Integer traversalDepth;

    public UUID getServiceId() {
        return serviceId;
    }

    public void setServiceId(UUID serviceId) {
        this.serviceId = serviceId;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public UUID getApiId() {
        return apiId;
    }

    public void setApiId(UUID apiId) {
        this.apiId = apiId;
    }

    public Integer getTraversalDepth() {
        return traversalDepth;
    }

    public void setTraversalDepth(Integer traversalDepth) {
        this.traversalDepth = traversalDepth;
    }
}
