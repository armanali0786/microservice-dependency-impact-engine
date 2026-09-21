package com.dependencyimpact.graphservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dependencyimpact.graphservice.entity.KafkaServiceBinding;
import java.util.UUID;

public interface KafkaServiceBindingRepository extends JpaRepository<KafkaServiceBinding, UUID> {
    java.util.List<KafkaServiceBinding> findByTopicId(UUID topicId);
    java.util.List<KafkaServiceBinding> findByServiceId(UUID serviceId);
}
