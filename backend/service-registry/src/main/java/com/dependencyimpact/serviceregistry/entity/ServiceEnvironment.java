package com.dependencyimpact.serviceregistry.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "service_environments")
public class ServiceEnvironment {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "service_id")
    private UUID serviceId;

    @Column(name = "environment")
    private String environment;

    @Column(name = "deployment_version")
    private String deploymentVersion;

    @Column(name = "status")
    private String status;

    @Column(name = "endpoint_url")
    private String endpointUrl;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
