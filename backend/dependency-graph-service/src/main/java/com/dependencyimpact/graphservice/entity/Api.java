package com.dependencyimpact.graphservice.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "apis")
public class Api {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "service_id")
    private UUID serviceId;

    @Column(name = "name")
    private String name;

    @Column(name = "path")
    private String path;

    @Column(name = "http_method")
    private String httpMethod;

    @Column(name = "version")
    private String version;

    @Column(name = "request_schema")
    private String requestSchema;

    @Column(name = "response_schema")
    private String responseSchema;

    @Column(name = "deprecated")
    private Boolean deprecated;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
