package com.dependencyimpact.graphservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dependencyimpact.graphservice.entity.Database;
import java.util.UUID;

public interface DatabaseRepository extends JpaRepository<Database, UUID> {
    java.util.Optional<Database> findByNameAndEnvironment(String name, String environment);
}
