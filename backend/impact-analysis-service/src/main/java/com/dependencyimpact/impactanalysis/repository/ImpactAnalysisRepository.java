package com.dependencyimpact.impactanalysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dependencyimpact.impactanalysis.entity.ImpactAnalysis;
import java.util.UUID;

public interface ImpactAnalysisRepository extends JpaRepository<ImpactAnalysis, UUID> {
}
