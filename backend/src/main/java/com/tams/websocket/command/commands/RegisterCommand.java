package com.tams.websocket.command.commands;

import com.tams.websocket.audit.Trace;
import com.tams.websocket.audit.TraceInfo;
import com.tams.websocket.command.commands.models.RegisterRequest;
import com.tams.websocket.command.commands.models.RegisterResponse;
import com.tams.websocket.datasource.entity.DeviceInfo;
import com.tams.websocket.datasource.entity.enums.DeviceStatus;
import com.tams.websocket.datasource.repository.DeviceInfoRepository;
import com.tams.websocket.socket.SessionContext;
import com.tams.websocket.socket.SessionInfo;
import com.tams.websocket.utils.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component("Register") // Use the command name as the bean name
@RequiredArgsConstructor // Lombok annotation to generate a constructor with required args
public class RegisterCommand implements Command {

    private RegisterCommand proxy;
    @Autowired
    private DeviceInfoRepository deviceInfoRepository;

    @Override
    public void init(Command command) {
        this.proxy = (RegisterCommand) command;
    }

    @Override
    public String sender(TraceInfo traceInfo) { return ""; }

    @Trace
    @Override
    public void execute(SessionInfo sessionInfo, TraceInfo traceInfo) {
        // Convert Input string to Request Object
        RegisterRequest request = Utilities.convertToMessage(sessionInfo.getMessageInput(), RegisterRequest.class);
        log.info("Execution:" + request);
        DeviceInfo deviceInfo = proxy.getOrCreateDeviceRegister(sessionInfo, request, traceInfo);
        proxy.sendResposne(sessionInfo.getSessionId(), deviceInfo, traceInfo);
    }

    /**
     * Checks if a record exists based on deviceSerialNo.
     * If it exists, fetches the record; otherwise, inserts a new record.
     *
     * @return the fetched or newly inserted DeviceRegister entity
     */
    @Trace
    @Transactional
    public DeviceInfo getOrCreateDeviceRegister(SessionInfo sessionInfo, RegisterRequest request, TraceInfo traceInfo) {

        // Check if the record exists
        Optional<DeviceInfo> existingDevice = deviceInfoRepository.findByDeviceSerialNo(request.getDeviceSerialNo());

        if (existingDevice.isPresent()) {
            return existingDevice.get();
        } else {
            DeviceInfo newDevice = DeviceInfo.builder()
                    .terminalType(request.getTerminalType())
                    .productName(request.getProductName())
                    .deviceSerialNo(request.getDeviceSerialNo())
                    .cloudId(request.getCloudId())
                    .token(Utilities.generateUUID4())
                    .hostAddress(sessionInfo.getHostAddress())
                    .hostPort(Integer.toString(sessionInfo.getHostPort()))
                    .sessionId(sessionInfo.getSessionId())
                    .status(DeviceStatus.REGISTERED)
                    .registeredOn(Utilities.getCurrentDateTime())
                    .build();
            return deviceInfoRepository.save(newDevice);
        }
    }

    @Trace
    public String sendResposne(String sessionId, DeviceInfo deviceInfo, TraceInfo traceInfo) {
        // Preparing response XML data
        RegisterResponse response = RegisterResponse.builder()
                .response("Register")
                .deviceSerialNo(deviceInfo.getDeviceSerialNo())
                .token(deviceInfo.getToken())
                .result("OK")
                .build();
        try {
            String finalResponse = Utilities.convertToXmlString(response);
            SessionContext.sendResponse(sessionId, finalResponse);
            return finalResponse;
        } catch (Exception e) {
            log.error("Error in send response for REGISTER:", e);
            return "{ \"error\" : "+e.getMessage()+"}";
        }
    }
}
