package com.dependencyimpact.notification.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Tracks every currently-connected, authenticated notification WebSocket
 * session. Broadcasts go to every session here - the platform doesn't yet
 * have per-channel subscriptions (docs/security.md #52 lists that as a
 * possible future refinement, not part of the MVP).
 */
@Component
public class WebSocketSessionRegistry {

    private final ConcurrentMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void register(WebSocketSession session) {
        sessions.put(session.getId(), session);
    }

    public void unregister(WebSocketSession session) {
        sessions.remove(session.getId());
    }

    public Collection<WebSocketSession> activeSessions() {
        return sessions.values();
    }
}
