package com.dependencyimpact.impactanalysis.repository;

import com.dependencyimpact.impactanalysis.entity.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, String> {
}
