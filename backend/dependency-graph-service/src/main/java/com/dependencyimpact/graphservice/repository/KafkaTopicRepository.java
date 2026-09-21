package com.dependencyimpact.graphservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dependencyimpact.graphservice.entity.KafkaTopic;
import java.util.UUID;

public interface KafkaTopicRepository extends JpaRepository<KafkaTopic, UUID> {
    java.util.Optional<KafkaTopic> findByNameAndEnvironment(String name, String environment);
}
