package com.dependencyimpact.impactanalysis.repository;

import com.dependencyimpact.impactanalysis.entity.ServiceLookup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ServiceLookupRepository extends JpaRepository<ServiceLookup, UUID> {
}
