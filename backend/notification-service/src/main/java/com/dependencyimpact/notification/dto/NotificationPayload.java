package com.dependencyimpact.notification.dto;

import com.dependencyimpact.common.events.EventType;

/**
 * Internal representation of "something happened that clients should know
 * about", before it's wrapped into the {@link WebSocketEventMessage} wire
 * format and broadcast.
 */
public class NotificationPayload {

    private final EventType eventType;
    private final Object data;

    public NotificationPayload(EventType eventType, Object data) {
        this.eventType = eventType;
        this.data = data;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Object getData() {
        return data;
    }
}
