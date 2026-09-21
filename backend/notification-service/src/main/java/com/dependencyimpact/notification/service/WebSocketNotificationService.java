package com.dependencyimpact.notification.service;

import com.dependencyimpact.notification.dto.NotificationPayload;
import com.dependencyimpact.notification.dto.WebSocketEventMessage;
import com.dependencyimpact.notification.exception.NotificationDeliveryException;
import com.dependencyimpact.notification.websocket.WebSocketSessionRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.Instant;

@Service
public class WebSocketNotificationService {

    private static final Logger log = LoggerFactory.getLogger(WebSocketNotificationService.class);

    private final WebSocketSessionRegistry sessionRegistry;
    private final ObjectMapper objectMapper;

    public WebSocketNotificationService(WebSocketSessionRegistry sessionRegistry, ObjectMapper objectMapper) {
        this.sessionRegistry = sessionRegistry;
        this.objectMapper = objectMapper;
    }

    public void broadcast(NotificationPayload payload) {
        WebSocketEventMessage message = new WebSocketEventMessage(
                payload.getEventType().name(), Instant.now(), payload.getData());

        String json;
        try {
            json = objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            throw new NotificationDeliveryException("Failed to serialize notification: " + e.getMessage());
        }

        TextMessage textMessage = new TextMessage(json);
        for (WebSocketSession session : sessionRegistry.activeSessions()) {
            if (!session.isOpen()) {
                continue;
            }
            try {
                session.sendMessage(textMessage);
            } catch (IOException e) {
                // One slow/dead client shouldn't stop the broadcast to everyone else.
                log.warn("Failed to send notification to session {}: {}", session.getId(), e.getMessage());
            }
        }
    }
}
