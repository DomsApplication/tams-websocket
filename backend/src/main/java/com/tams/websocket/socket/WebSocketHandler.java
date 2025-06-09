package com.tams.websocket.socket;

import com.tams.websocket.command.CommandHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {



    @Autowired
    private CommandHandler commandHandler;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        SessionContext.putSession(session);
        log.info(session.getId() + ": CONNECTED.....");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            SessionInfo sessionInfo = SessionInfo.builder()
                    .hostAddress(session.getRemoteAddress().getAddress().getHostAddress())
                    .hostPort(session.getRemoteAddress().getPort())
                    .messageInput(message.getPayload())
                    .sessionId(session.getId())
                    .build();
            log.info(session.getId() + ": MESSAGE.....");
            commandHandler.handleRequest(sessionInfo);
        } catch (Exception e) {
            log.error("Exception in handleTextMessage:", e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        SessionContext.removeSession(session.getId());
        log.info(session.getId() + ": CLOSED.....");
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info(session.getId() + ": ERROR....." + exception.getMessage());
    }

}
