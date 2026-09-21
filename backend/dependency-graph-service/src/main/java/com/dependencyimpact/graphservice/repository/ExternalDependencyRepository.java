package com.dependencyimpact.graphservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dependencyimpact.graphservice.entity.ExternalDependency;
import java.util.UUID;

public interface ExternalDependencyRepository extends JpaRepository<ExternalDependency, UUID> {
    java.util.List<ExternalDependency> findByServiceId(UUID serviceId);
}
