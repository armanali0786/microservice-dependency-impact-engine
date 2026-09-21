package com.dependencyimpact.serviceregistry.dto;

import java.time.Instant;
import java.util.UUID;

public class ServiceResponse {

    private final UUID id;
    private final UUID teamId;
    private final String name;
    private final String description;
    private final String technology;
    private final String repositoryUrl;
    private final String repositoryName;
    private final String status;
    private final String version;
    private final Instant createdAt;
    private final Instant updatedAt;

    public ServiceResponse(UUID id, UUID teamId, String name, String description, String technology,
                            String repositoryUrl, String repositoryName, String status, String version,
                            Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.teamId = teamId;
        this.name = name;
        this.description = description;
        this.technology = technology;
        this.repositoryUrl = repositoryUrl;
        this.repositoryName = repositoryName;
        this.status = status;
        this.version = version;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getTeamId() {
        return teamId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTechnology() {
        return technology;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public String getStatus() {
        return status;
    }

    public String getVersion() {
        return version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
