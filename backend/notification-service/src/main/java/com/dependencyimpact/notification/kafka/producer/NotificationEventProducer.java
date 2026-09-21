package com.dependencyimpact.notification.kafka.producer;

import com.dependencyimpact.common.events.EventEnvelope;
import com.dependencyimpact.common.events.EventType;
import com.dependencyimpact.common.observability.CorrelationIdHolder;
import com.dependencyimpact.notification.dto.NotificationPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

/**
 * Republishes every notification to the {@code notification-events} topic
 * (docs README #20), so future consumers - email, Slack, audit trail - can
 * react to the same events without notification-service knowing about them.
 */
@Component
public class NotificationEventProducer {

    private static final Logger log = LoggerFactory.getLogger(NotificationEventProducer.class);
    private static final String TOPIC = "notification-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.application.name}")
    private String applicationName;

    public NotificationEventProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(NotificationPayload payload) {
        EventEnvelope<Object> envelope = new EventEnvelope<>(
                UUID.randomUUID().toString(),
                EventType.NOTIFICATION_REQUESTED,
                1,
                applicationName,
                null,
                Instant.now(),
                CorrelationIdHolder.get(),
                null,
                payload.getData());

        try {
            kafkaTemplate.send(TOPIC, envelope.eventId(), objectMapper.writeValueAsString(envelope));
        } catch (Exception e) {
            // A failed publish here shouldn't fail the WebSocket broadcast that
            // already reached connected clients - just log it.
            log.warn("Failed to publish to {}: {}", TOPIC, e.getMessage());
        }
    }
}
