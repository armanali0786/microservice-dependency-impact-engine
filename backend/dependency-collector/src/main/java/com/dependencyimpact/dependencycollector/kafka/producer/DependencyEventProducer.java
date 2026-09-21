package com.dependencyimpact.dependencycollector.kafka.producer;

import com.dependencyimpact.common.events.EventEnvelope;
import com.dependencyimpact.common.events.EventType;
import com.dependencyimpact.common.model.DependencyObservation;
import com.dependencyimpact.dependencycollector.exception.CollectionException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class DependencyEventProducer {

    private static final String TOPIC = "dependency-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public DependencyEventProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishDependencyDiscovered(DependencyObservation observation) {
        EventEnvelope<DependencyObservation> envelope = new EventEnvelope<>(
                UUID.randomUUID().toString(),
                EventType.DEPENDENCY_DISCOVERED,
                1,
                "dependency-collector",
                observation.environment(),
                Instant.now(),
                null,
                null,
                observation
        );

        try {
            String payload = objectMapper.writeValueAsString(envelope);
            // Keying by source service means all events about the same service
            // land on the same partition, so a consumer sees them in order.
            kafkaTemplate.send(TOPIC, observation.sourceServiceId(), payload);
        } catch (Exception e) {
            throw new CollectionException("Failed to publish dependency-discovered event: " + e.getMessage());
        }
    }
}
