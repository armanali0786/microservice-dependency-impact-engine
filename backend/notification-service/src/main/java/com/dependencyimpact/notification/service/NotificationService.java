package com.dependencyimpact.notification.service;

import com.dependencyimpact.common.events.EventType;
import com.dependencyimpact.notification.dto.NotificationPayload;
import com.dependencyimpact.notification.kafka.producer.NotificationEventProducer;

/**
 * Single entry point every Kafka consumer in this service calls: broadcast
 * the event to connected WebSocket clients, then republish it to
 * notification-events for any other interested consumer.
 */
@org.springframework.stereotype.Service
public class NotificationService {

    private final WebSocketNotificationService webSocketNotificationService;
    private final NotificationEventProducer notificationEventProducer;

    public NotificationService(WebSocketNotificationService webSocketNotificationService,
            NotificationEventProducer notificationEventProducer) {
        this.webSocketNotificationService = webSocketNotificationService;
        this.notificationEventProducer = notificationEventProducer;
    }

    public void notify(EventType eventType, Object data) {
        NotificationPayload payload = new NotificationPayload(eventType, data);
        webSocketNotificationService.broadcast(payload);
        notificationEventProducer.publish(payload);
    }
}
