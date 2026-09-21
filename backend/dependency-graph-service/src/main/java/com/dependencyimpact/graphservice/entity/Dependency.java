package com.dependencyimpact.graphservice.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "dependencies")
public class Dependency {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "source_service_id")
    private UUID sourceServiceId;

    @Column(name = "target_service_id")
    private UUID targetServiceId;

    @Column(name = "dependency_type")
    private String dependencyType;

    @Column(name = "protocol")
    private String protocol;

    @Column(name = "api_id")
    private UUID apiId;

    @Column(name = "endpoint")
    private String endpoint;

    @Column(name = "topic_name")
    private String topicName;

    @Column(name = "environment")
    private String environment;

    @Column(name = "criticality")
    private String criticality;

    @Column(name = "failure_behavior")
    private String failureBehavior;

    @Column(name = "confidence")
    private String confidence;

    @Column(name = "latency_ms")
    private Double latencyMs;

    @Column(name = "error_rate")
    private Double errorRate;

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
