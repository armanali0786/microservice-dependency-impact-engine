package com.dependencyimpact.impactanalysis.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "impact_components")
public class ImpactComponent {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "impact_analysis_id")
    private UUID impactAnalysisId;

    @Column(name = "service_id")
    private UUID serviceId;

    @Column(name = "dependency_type")
    private String dependencyType;

    @Column(name = "depth")
    private Integer depth;

    @Column(name = "impact_level")
    private String impactLevel;

    @Column(name = "reason")
    private String reason;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "dependency_path")
    private String dependencyPath;

    @Column(name = "runtime_evidence")
    private Boolean runtimeEvidence;

    @Column(name = "critical_dependency")
    private Boolean criticalDependency;

    @Column(name = "created_at")
    private Instant createdAt;

    public ImpactComponent() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getImpactAnalysisId() {
        return impactAnalysisId;
    }

    public void setImpactAnalysisId(UUID impactAnalysisId) {
        this.impactAnalysisId = impactAnalysisId;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public void setServiceId(UUID serviceId) {
        this.serviceId = serviceId;
    }

    public String getDependencyType() {
        return dependencyType;
    }

    public void setDependencyType(String dependencyType) {
        this.dependencyType = dependencyType;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public String getImpactLevel() {
        return impactLevel;
    }

    public void setImpactLevel(String impactLevel) {
        this.impactLevel = impactLevel;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDependencyPath() {
        return dependencyPath;
    }

    public void setDependencyPath(String dependencyPath) {
        this.dependencyPath = dependencyPath;
    }

    public Boolean getRuntimeEvidence() {
        return runtimeEvidence;
    }

    public void setRuntimeEvidence(Boolean runtimeEvidence) {
        this.runtimeEvidence = runtimeEvidence;
    }

    public Boolean getCriticalDependency() {
        return criticalDependency;
    }

    public void setCriticalDependency(Boolean criticalDependency) {
        this.criticalDependency = criticalDependency;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
