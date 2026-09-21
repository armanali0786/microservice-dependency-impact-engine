package com.dependencyimpact.serviceregistry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dependencyimpact.serviceregistry.entity.AuditLog;
import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    java.util.List<AuditLog> findByUserId(UUID userId);
}
