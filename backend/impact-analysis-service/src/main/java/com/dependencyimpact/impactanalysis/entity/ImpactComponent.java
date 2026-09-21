package com.dependencyimpact.impactanalysis.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

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

    @Column(name = "dependency_path")
    private String dependencyPath;

    @Column(name = "runtime_evidence")
    private Boolean runtimeEvidence;

    @Column(name = "critical_dependency")
    private Boolean criticalDependency;

    @Column(name = "created_at")
    private Instant createdAt;
}
