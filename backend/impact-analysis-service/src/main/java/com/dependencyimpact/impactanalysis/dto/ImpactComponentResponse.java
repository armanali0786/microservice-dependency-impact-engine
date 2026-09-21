package com.dependencyimpact.impactanalysis.dto;

import java.util.List;
import java.util.UUID;

public class ImpactComponentResponse {

    private final UUID serviceId;
    private final String service;
    private final Integer depth;
    private final String impactLevel;
    private final String reason;
    private final String dependencyType;
    private final boolean criticalDependency;
    private final boolean runtimeEvidence;
    private final List<String> dependencyPath;

    public ImpactComponentResponse(UUID serviceId, String service, Integer depth, String impactLevel,
                                    String reason, String dependencyType, boolean criticalDependency,
                                    boolean runtimeEvidence, List<String> dependencyPath) {
        this.serviceId = serviceId;
        this.service = service;
        this.depth = depth;
        this.impactLevel = impactLevel;
        this.reason = reason;
        this.dependencyType = dependencyType;
        this.criticalDependency = criticalDependency;
        this.runtimeEvidence = runtimeEvidence;
        this.dependencyPath = dependencyPath;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public String getService() {
        return service;
    }

    public Integer getDepth() {
        return depth;
    }

    public String getImpactLevel() {
        return impactLevel;
    }

    public String getReason() {
        return reason;
    }

    public String getDependencyType() {
        return dependencyType;
    }

    public boolean isCriticalDependency() {
        return criticalDependency;
    }

    public boolean isRuntimeEvidence() {
        return runtimeEvidence;
    }

    public List<String> getDependencyPath() {
        return dependencyPath;
    }
}
