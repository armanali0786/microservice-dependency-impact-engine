package com.dependencyimpact.graphservice.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "kafka_topics")
public class KafkaTopic {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "environment")
    private String environment;

    @Column(name = "description")
    private String description;

    @Column(name = "schema_version")
    private String schemaVersion;

    @Column(name = "partition_count")
    private Integer partitionCount;

    @Column(name = "retention_ms")
    private Long retentionMs;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
