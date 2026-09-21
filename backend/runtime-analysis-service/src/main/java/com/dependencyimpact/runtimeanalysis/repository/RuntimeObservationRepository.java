package com.dependencyimpact.runtimeanalysis.repository;

import com.dependencyimpact.runtimeanalysis.entity.RuntimeObservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface RuntimeObservationRepository extends JpaRepository<RuntimeObservation, UUID> {

    java.util.List<RuntimeObservation> findBySourceServiceIdAndEnvironment(UUID sourceServiceId, String environment);

    // "Runtime behavior of service X" means calls INTO X, i.e. X is the target -
    // same convention the metrics/incident-detection queries below use.
    List<RuntimeObservation> findByTargetServiceIdAndEnvironmentAndObservedAtBetween(
            UUID targetServiceId, String environment, Instant from, Instant to);

    interface ServiceEnvironmentPair {
        UUID getTargetServiceId();
        String getEnvironment();
    }

    @Query("SELECT DISTINCT r.targetServiceId as targetServiceId, r.environment as environment " +
            "FROM RuntimeObservation r WHERE r.observedAt >= :since AND r.targetServiceId IS NOT NULL")
    List<ServiceEnvironmentPair> findDistinctTargetServiceEnvironmentPairsSince(@Param("since") Instant since);
}
