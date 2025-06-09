package com.tams.websocket.socket;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Builder;
import org.springframework.web.socket.WebSocketSession;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class SessionInfo {

    private String sessionId;
    private String hostAddress;
    private int hostPort;
    private String command;
    private String deviceSerialNo;
    private String messageInput;
    private WebSocketSession session;
}
