package com.tams.websocket.command;

import com.tams.websocket.audit.TraceInfo;
import com.tams.websocket.command.commands.Command;
import com.tams.websocket.datasource.entity.enums.UserFlagStatus;
import com.tams.websocket.datasource.repository.DeviceCommandsRepositoryCustom;
import com.tams.websocket.datasource.repository.UserCommandsRepositoryCustom;
import com.tams.websocket.socket.SessionContext;
import com.tams.websocket.utils.Utilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Component
public class SenderHandler {

    private final ApplicationContext applicationContext;

    @Autowired
    private UserCommandsRepositoryCustom userCommandsRepositoryCustom;

    @Autowired
    private DeviceCommandsRepositoryCustom deviceCommandsRepositoryCustom;

    @Autowired
    public SenderHandler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Async("taskExecutor")
    public void sendRequest(String deviceSerialNo, String sessionId, String userId, String command, String commandType) {
        WebSocketSession socketSession = null;
        try {
            socketSession = SessionContext.getSocketSession(sessionId);
            TraceInfo traceInfo = TraceInfo.builder()
                    .traceId(Utilities.generateUUID4())
                    .sessionId(sessionId)
                    .command(command)
                    .deviceSerialNo(deviceSerialNo)
                    .hostAddress(socketSession.getRemoteAddress().getAddress().getHostAddress())
                    .hostPort(Integer.toString(socketSession.getRemoteAddress().getPort()))
                    .userId(userId)
                    .build();

            /* Update Command Flag to 'PROCESSING'*/
            this.updateCommandStatus(command, UserFlagStatus.PROCESSING, deviceSerialNo, userId, commandType);

            Command serviceInstance = (Command) applicationContext.getBean(command);
            serviceInstance.init(serviceInstance);
            serviceInstance.sender(traceInfo);

            /* Update Command Flag to 'PROCESSED'*/
            this.updateCommandStatus(command, UserFlagStatus.PROCESSED, deviceSerialNo, userId, commandType);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != socketSession) {
                    socketSession = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param command
     * @param status
     * @param deviceSerialNo
     * @param userId
     */
    private void updateCommandStatus(String command, UserFlagStatus status, String deviceSerialNo, String userId,
                                     String commandType) {
        if (commandType.equals("UserType")) {
            userCommandsRepositoryCustom.updateSingleUserData(Utilities.lowercaseFirstLetter(command), status.name(),
                    deviceSerialNo, userId);
        } else if (commandType.equals("DeviceType")) {
            deviceCommandsRepositoryCustom.updateSingleDeviceData(Utilities.lowercaseFirstLetter(command), status.name(),
                    deviceSerialNo);
        }
    }
}
