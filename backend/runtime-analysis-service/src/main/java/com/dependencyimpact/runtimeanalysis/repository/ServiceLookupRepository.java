package com.dependencyimpact.runtimeanalysis.repository;

import com.dependencyimpact.runtimeanalysis.entity.ServiceLookup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ServiceLookupRepository extends JpaRepository<ServiceLookup, UUID> {
}
