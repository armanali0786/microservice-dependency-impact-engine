package com.dependencyimpact.runtimeanalysis.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "runtime_observations")
public class RuntimeObservation {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "trace_id")
    private String traceId;

    @Column(name = "span_id")
    private String spanId;

    @Column(name = "source_service_id")
    private UUID sourceServiceId;

    @Column(name = "target_service_id")
    private UUID targetServiceId;

    @Column(name = "endpoint")
    private String endpoint;

    @Column(name = "http_method")
    private String httpMethod;

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "latency_ms")
    private Double latencyMs;

    @Column(name = "observed_at")
    private Instant observedAt;

    @Column(name = "environment")
    private String environment;

    @Column(name = "metadata")
    private String metadata;
}
