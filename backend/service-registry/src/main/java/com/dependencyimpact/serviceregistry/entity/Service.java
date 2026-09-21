package com.dependencyimpact.serviceregistry.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "services")
public class Service {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "team_id")
    private UUID teamId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "technology")
    private String technology;

    @Column(name = "repository_url")
    private String repositoryUrl;

    @Column(name = "repository_name")
    private String repositoryName;

    @Column(name = "status")
    private String status;

    @Column(name = "version")
    private String version;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
