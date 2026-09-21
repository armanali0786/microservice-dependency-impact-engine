package com.dependencyimpact.serviceregistry.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "action")
    private String action;

    @Column(name = "resource_type")
    private String resourceType;

    @Column(name = "resource_id")
    private UUID resourceId;

    @Column(name = "correlation_id")
    private String correlationId;

    @Column(name = "metadata")
    private String metadata;

    @Column(name = "created_at")
    private Instant createdAt;
}
