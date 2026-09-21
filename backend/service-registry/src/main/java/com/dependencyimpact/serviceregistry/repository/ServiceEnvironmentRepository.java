package com.dependencyimpact.serviceregistry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dependencyimpact.serviceregistry.entity.ServiceEnvironment;
import java.util.UUID;

public interface ServiceEnvironmentRepository extends JpaRepository<ServiceEnvironment, UUID> {
    java.util.List<ServiceEnvironment> findByServiceId(UUID serviceId);
}
