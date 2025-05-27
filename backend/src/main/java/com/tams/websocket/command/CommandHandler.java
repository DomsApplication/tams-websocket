package com.tams.websocket.command;

import com.tams.websocket.audit.TraceInfo;
import com.tams.websocket.command.commands.Command;
import com.tams.websocket.socket.SessionInfo;
import com.tams.websocket.utils.Utilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommandHandler {

    private final ApplicationContext applicationContext;


    @Autowired
    public CommandHandler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void handleRequest(SessionInfo sessionInfo) {
        try {
            String command = Utilities.getRequestRouteType(sessionInfo.getMessageInput(), "Request");
            if(null == command) {
                command = Utilities.getRequestRouteType(sessionInfo.getMessageInput(), "Event");
            }
            if(null == command) {
                command = Utilities.getRequestRouteType(sessionInfo.getMessageInput(), "Response");
            }

            String deviceSerialNo = Utilities
                    .getRequestRouteType(sessionInfo.getMessageInput(), "DeviceSerialNo");
            sessionInfo.setCommand(command);
            sessionInfo.setDeviceSerialNo(deviceSerialNo);
            log.info(sessionInfo.getSessionId()+":::"+sessionInfo.toString());

            TraceInfo traceInfo = TraceInfo.builder()
                    .traceId(Utilities.generateUUID4())
                    .sessionId(sessionInfo.getSessionId())
                    .hostAddress(sessionInfo.getHostAddress())
                    .hostPort(Integer.toString(sessionInfo.getHostPort()))
                    .deviceSerialNo(deviceSerialNo)
                    .command(command)
                    .build();

            Command serviceInstance = (Command) applicationContext.getBean(command);
            serviceInstance.init(serviceInstance);
            serviceInstance.execute(sessionInfo, traceInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
