package com.dependencyimpact.graphservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dependencyimpact.graphservice.entity.Api;
import java.util.UUID;

public interface ApiRepository extends JpaRepository<Api, UUID> {
    java.util.List<Api> findByServiceId(UUID serviceId);
}
