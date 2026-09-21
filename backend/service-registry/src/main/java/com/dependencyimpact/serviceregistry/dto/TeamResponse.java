package com.dependencyimpact.serviceregistry.dto;

import java.time.Instant;
import java.util.UUID;

public class TeamResponse {

    private final UUID id;
    private final String name;
    private final String description;
    private final String ownerEmail;
    private final Instant createdAt;
    private final Instant updatedAt;

    public TeamResponse(UUID id, String name, String description, String ownerEmail,
                         Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerEmail = ownerEmail;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
