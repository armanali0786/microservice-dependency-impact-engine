package com.dependencyimpact.graphservice.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "kafka_service_bindings")
public class KafkaServiceBinding {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "service_id")
    private UUID serviceId;

    @Column(name = "topic_id")
    private UUID topicId;

    @Column(name = "binding_type")
    private String bindingType;

    @Column(name = "consumer_group")
    private String consumerGroup;

    @Column(name = "schema_version")
    private String schemaVersion;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
