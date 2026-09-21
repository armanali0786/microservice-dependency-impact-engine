package com.dependencyimpact.runtimeanalysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dependencyimpact.runtimeanalysis.entity.RuntimeObservation;
import java.util.UUID;

public interface RuntimeObservationRepository extends JpaRepository<RuntimeObservation, UUID> {
    java.util.List<RuntimeObservation> findBySourceServiceIdAndEnvironment(UUID sourceServiceId, String environment);
}
