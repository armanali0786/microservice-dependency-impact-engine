package com.dependencyimpact.impactanalysis.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "impact_analyses")
public class ImpactAnalysis {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "source_service_id")
    private UUID sourceServiceId;

    @Column(name = "change_type")
    private String changeType;

    @Column(name = "environment")
    private String environment;

    @Column(name = "api_id")
    private UUID apiId;

    @Column(name = "topic_id")
    private UUID topicId;

    @Column(name = "traversal_depth")
    private Integer traversalDepth;

    @Column(name = "status")
    private String status;

    @Column(name = "risk_level")
    private String riskLevel;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "created_at")
    private Instant createdAt;
}
