package com.dependencyimpact.runtimeanalysis.dto;

import java.util.List;
import java.util.UUID;

public class IncidentImpactResponse {

    private final UUID incidentId;
    private final List<String> affectedServices;
    private final List<List<String>> dependencyPaths;

    public IncidentImpactResponse(UUID incidentId, List<String> affectedServices, List<List<String>> dependencyPaths) {
        this.incidentId = incidentId;
        this.affectedServices = affectedServices;
        this.dependencyPaths = dependencyPaths;
    }

    public UUID getIncidentId() {
        return incidentId;
    }

    public List<String> getAffectedServices() {
        return affectedServices;
    }

    public List<List<String>> getDependencyPaths() {
        return dependencyPaths;
    }
}
