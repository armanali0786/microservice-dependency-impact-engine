package com.dependencyimpact.notification.kafka.consumer;

import com.dependencyimpact.common.events.EventEnvelope;
import com.dependencyimpact.common.exceptions.NonRetryableEventException;
import com.dependencyimpact.notification.service.NotificationService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class IncidentEventConsumer {

    private static final String CONSUMER_GROUP = "notification-service-consumers";

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    public IncidentEventConsumer(ObjectMapper objectMapper, NotificationService notificationService) {
        this.objectMapper = objectMapper;
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "incident-events", groupId = CONSUMER_GROUP)
    public void onMessage(String message) {
        EventEnvelope<Object> envelope;
        try {
            envelope = objectMapper.readValue(message, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new NonRetryableEventException("Malformed incident-events message: " + message, e);
        }

        notificationService.notify(envelope.eventType(), envelope.payload());
    }
}
