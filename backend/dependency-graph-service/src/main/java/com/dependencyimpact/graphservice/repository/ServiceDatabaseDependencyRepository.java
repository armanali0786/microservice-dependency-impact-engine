package com.dependencyimpact.graphservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dependencyimpact.graphservice.entity.ServiceDatabaseDependency;
import java.util.UUID;

public interface ServiceDatabaseDependencyRepository extends JpaRepository<ServiceDatabaseDependency, UUID> {
    java.util.List<ServiceDatabaseDependency> findByServiceId(UUID serviceId);
}
