package com.tams.websocket.socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SessionContext {

    private static final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public static void putSession(WebSocketSession session) {
        sessions.put(session.getId(), session);
    }

    public static void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public static void sendResponse(String sessionId, String message) throws IOException {
        try {
            sessions.get(sessionId).sendMessage(new TextMessage(message));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Exception in sendResponse for the message::: " + message +"\n", e);
        }
    }

    public static WebSocketSession getSocketSession(String sessionId) {
        return sessions.get(sessionId);
    }

}
