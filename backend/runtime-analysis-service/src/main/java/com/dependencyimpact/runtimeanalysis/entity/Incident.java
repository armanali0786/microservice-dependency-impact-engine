package com.dependencyimpact.runtimeanalysis.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "incidents")
public class Incident {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "service_id")
    private UUID serviceId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "severity")
    private String severity;

    @Column(name = "status")
    private String status;

    @Column(name = "error_rate")
    private Double errorRate;

    @Column(name = "latency_ms")
    private Double latencyMs;

    @Column(name = "kafka_lag")
    private Long kafkaLag;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "resolved_at")
    private Instant resolvedAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
