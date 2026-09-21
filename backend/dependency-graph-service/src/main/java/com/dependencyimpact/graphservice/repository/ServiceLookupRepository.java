package com.dependencyimpact.graphservice.repository;

import com.dependencyimpact.graphservice.entity.ServiceLookup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ServiceLookupRepository extends JpaRepository<ServiceLookup, UUID> {
    Optional<ServiceLookup> findByName(String name);
}
