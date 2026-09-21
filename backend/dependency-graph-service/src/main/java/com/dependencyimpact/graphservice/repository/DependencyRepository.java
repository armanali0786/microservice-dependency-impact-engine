package com.dependencyimpact.graphservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dependencyimpact.graphservice.entity.Dependency;
import java.util.UUID;

public interface DependencyRepository extends JpaRepository<Dependency, UUID> {
    java.util.List<Dependency> findBySourceServiceIdAndEnvironment(UUID sourceServiceId, String environment);
    java.util.List<Dependency> findByTargetServiceIdAndEnvironment(UUID targetServiceId, String environment);
}
