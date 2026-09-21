package com.dependencyimpact.serviceregistry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dependencyimpact.serviceregistry.entity.Team;
import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, UUID> {
    java.util.Optional<Team> findByName(String name);
}
