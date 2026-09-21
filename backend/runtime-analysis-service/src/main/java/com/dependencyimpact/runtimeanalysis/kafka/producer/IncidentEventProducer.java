package com.dependencyimpact.runtimeanalysis.kafka.producer;

import com.dependencyimpact.common.events.EventEnvelope;
import com.dependencyimpact.common.events.EventType;
import com.dependencyimpact.runtimeanalysis.dto.IncidentCreatedPayload;
import com.dependencyimpact.runtimeanalysis.dto.IncidentResolvedPayload;
import com.dependencyimpact.runtimeanalysis.exception.RuntimeAnalysisException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class IncidentEventProducer {

    private static final String TOPIC = "incident-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public IncidentEventProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishCreated(IncidentCreatedPayload payload) {
        publish(EventType.INCIDENT_CREATED, payload, payload.incidentId());
    }

    public void publishResolved(IncidentResolvedPayload payload) {
        publish(EventType.INCIDENT_RESOLVED, payload, payload.incidentId());
    }

    private void publish(EventType eventType, Object payload, UUID incidentId) {
        EventEnvelope<Object> envelope = new EventEnvelope<>(
                UUID.randomUUID().toString(),
                eventType,
                1,
                "runtime-analysis-service",
                null,
                Instant.now(),
                null,
                null,
                payload
        );

        try {
            String json = objectMapper.writeValueAsString(envelope);
            kafkaTemplate.send(TOPIC, incidentId.toString(), json);
        } catch (Exception e) {
            throw new RuntimeAnalysisException("Failed to publish " + eventType + " event: " + e.getMessage());
        }
    }
}
