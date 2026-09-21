package com.dependencyimpact.notification.dto;

import java.time.Instant;

/**
 * The wire format pushed to connected clients - matches the shape documented
 * in docs/api-structure.md #81.
 */
public class WebSocketEventMessage {

    private final String eventType;
    private final Instant timestamp;
    private final Object data;

    public WebSocketEventMessage(String eventType, Instant timestamp, Object data) {
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.data = data;
    }

    public String getEventType() {
        return eventType;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Object getData() {
        return data;
    }
}
