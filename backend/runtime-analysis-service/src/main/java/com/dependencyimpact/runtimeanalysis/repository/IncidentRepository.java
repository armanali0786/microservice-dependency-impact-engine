package com.dependencyimpact.runtimeanalysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dependencyimpact.runtimeanalysis.entity.Incident;
import java.util.UUID;

public interface IncidentRepository extends JpaRepository<Incident, UUID> {
    java.util.List<Incident> findByServiceId(UUID serviceId);
    java.util.List<Incident> findByStatus(String status);
}
