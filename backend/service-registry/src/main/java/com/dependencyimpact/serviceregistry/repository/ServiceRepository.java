package com.dependencyimpact.serviceregistry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dependencyimpact.serviceregistry.entity.Service;
import java.util.UUID;

public interface ServiceRepository extends JpaRepository<Service, UUID> {
    java.util.Optional<Service> findByName(String name);
    java.util.List<Service> findByTeamId(UUID teamId);
}
