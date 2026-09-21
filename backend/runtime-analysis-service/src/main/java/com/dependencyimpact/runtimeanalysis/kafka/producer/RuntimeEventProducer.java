package com.dependencyimpact.runtimeanalysis.kafka.producer;

import com.dependencyimpact.common.events.EventEnvelope;
import com.dependencyimpact.common.events.EventType;
import com.dependencyimpact.runtimeanalysis.dto.RuntimeObservationReceivedPayload;
import com.dependencyimpact.runtimeanalysis.exception.RuntimeAnalysisException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

// Nothing consumes this topic yet - dependency-graph-service "strengthening" a
// dependency's confidence from runtime evidence (docs/technical-implementation.md
// section 30) is deferred, not implemented. Publishing the event anyway keeps this
// service decoupled from whoever ends up consuming it, same as DEPENDENCY_DISCOVERED
// in dependency-collector.
@Component
public class RuntimeEventProducer {

    private static final String TOPIC = "runtime-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public RuntimeEventProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishObservationReceived(RuntimeObservationReceivedPayload payload) {
        EventEnvelope<RuntimeObservationReceivedPayload> envelope = new EventEnvelope<>(
                UUID.randomUUID().toString(),
                EventType.RUNTIME_OBSERVATION_RECEIVED,
                1,
                "runtime-analysis-service",
                payload.environment(),
                Instant.now(),
                null,
                null,
                payload
        );

        try {
            String json = objectMapper.writeValueAsString(envelope);
            kafkaTemplate.send(TOPIC, payload.targetServiceId().toString(), json);
        } catch (Exception e) {
            throw new RuntimeAnalysisException("Failed to publish RUNTIME_OBSERVATION_RECEIVED event: " + e.getMessage());
        }
    }
}
