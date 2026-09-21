package com.dependencyimpact.graphservice.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "external_dependencies")
public class ExternalDependency {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "service_id")
    private UUID serviceId;

    @Column(name = "name")
    private String name;

    @Column(name = "dependency_type")
    private String dependencyType;

    @Column(name = "endpoint")
    private String endpoint;

    @Column(name = "environment")
    private String environment;

    @Column(name = "criticality")
    private String criticality;

    @Column(name = "failure_behavior")
    private String failureBehavior;

    @Column(name = "first_seen_at")
    private Instant firstSeenAt;

    @Column(name = "last_seen_at")
    private Instant lastSeenAt;

    @Column(name = "status")
    private String status;

    @Column(name = "metadata")
    private String metadata;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
