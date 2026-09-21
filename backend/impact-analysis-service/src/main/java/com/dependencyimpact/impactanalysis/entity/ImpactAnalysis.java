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

    public ImpactAnalysis() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSourceServiceId() {
        return sourceServiceId;
    }

    public void setSourceServiceId(UUID sourceServiceId) {
        this.sourceServiceId = sourceServiceId;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public UUID getApiId() {
        return apiId;
    }

    public void setApiId(UUID apiId) {
        this.apiId = apiId;
    }

    public UUID getTopicId() {
        return topicId;
    }

    public void setTopicId(UUID topicId) {
        this.topicId = topicId;
    }

    public Integer getTraversalDepth() {
        return traversalDepth;
    }

    public void setTraversalDepth(Integer traversalDepth) {
        this.traversalDepth = traversalDepth;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
