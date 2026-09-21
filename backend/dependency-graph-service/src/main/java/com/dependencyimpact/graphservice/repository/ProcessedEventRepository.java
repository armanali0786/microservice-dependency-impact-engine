package com.dependencyimpact.graphservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dependencyimpact.graphservice.entity.ProcessedEvent;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, String> {
}
