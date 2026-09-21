package com.dependencyimpact.impactanalysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dependencyimpact.impactanalysis.entity.ImpactComponent;
import java.util.UUID;

public interface ImpactComponentRepository extends JpaRepository<ImpactComponent, UUID> {
    java.util.List<ImpactComponent> findByImpactAnalysisId(UUID impactAnalysisId);
}
