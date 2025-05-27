package com.tams.websocket.command.commands;

import com.tams.websocket.audit.Trace;
import com.tams.websocket.audit.TraceInfo;
import com.tams.websocket.command.commands.models.LoginRequest;
import com.tams.websocket.command.commands.models.LoginResponse;
import com.tams.websocket.datasource.entity.DeviceInfo;
import com.tams.websocket.datasource.entity.enums.DeviceStatus;
import com.tams.websocket.datasource.entity.enums.UserFlagStatus;
import com.tams.websocket.datasource.repository.DeviceCommandsRepositoryCustom;
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
@Component("Login")
@RequiredArgsConstructor // Lombok annotation to generate a constructor with required args
public class LoginCommand implements Command {

    private LoginCommand proxy;
    @Autowired
    private DeviceInfoRepository deviceInfoRepository;

    @Autowired
    private DeviceCommandsRepositoryCustom deviceCommandsRepositoryCustom;

    @Override
    public void init(Command command) {
        this.proxy = (LoginCommand) command;
    }

    @Override
    public String sender(TraceInfo traceInfo) { return ""; }

    @Trace
    @Override
    public void execute(SessionInfo sessionInfo, TraceInfo traceInfo) {
        LoginRequest request = Utilities.convertToMessage(sessionInfo.getMessageInput(), LoginRequest.class);
        log.info("Execution:" + request);

        String result = proxy.isDeviceValid(request.getDeviceSerialNo(), request.getToken(), traceInfo);

        proxy.sendResposne(sessionInfo.getSessionId(), request, result, traceInfo);

        proxy.syncServerTimeToDevice(request.getDeviceSerialNo(), traceInfo);
    }

    @Trace
    public String isDeviceValid(String deviceSerialNo, String token, TraceInfo traceInfo) {
        Optional<DeviceInfo> deviceInfoOptional = deviceInfoRepository.findByDeviceSerialNo(deviceSerialNo);

        return deviceInfoOptional.map(deviceInfo -> {
            if (deviceInfo.getToken() != null && deviceInfo.getToken().equals(token)) {
                proxy.updateDeviceRegister(deviceInfo, traceInfo);
                return "OK"; // Token is valid
            } else {
                return "FailUnknownToken"; // Token is invalid or does not exist
            }
        //}).orElse("Fail"); // Device not found
        }).orElse("FailUnknownToken"); // Device not found
    }

    @Trace
    @Transactional
    public void updateDeviceRegister(DeviceInfo deviceInfo, TraceInfo traceInfo) {
        deviceInfo.setStatus(DeviceStatus.LOGGED);
        deviceInfo.setLoggedOn(Utilities.getCurrentDateTime());
        deviceInfoRepository.save(deviceInfo);
    }

    @Trace
    public String sendResposne(String sessionId, LoginRequest request, String status, TraceInfo traceInfo) {
        LoginResponse response = LoginResponse.builder()
                .response("Login")
                .deviceSerialNo(request.getDeviceSerialNo())
                .result(status)
                .build();
        try {
            String finalResponse = Utilities.convertToXmlString(response);
            SessionContext.sendResponse(sessionId, finalResponse);
            return finalResponse;
        } catch (Exception e) {
            log.error("Error in send response for LOGIN:", e);
            return "{ \"error\" : "+e.getMessage()+"}";
        }
    }

    @Trace
    @Transactional
    public void syncServerTimeToDevice(String deviceSerialNo, TraceInfo traceInfo) {
        /* Update SetTime Command Flag to 'NEW' */
        deviceCommandsRepositoryCustom.updateSingleDeviceData("setTime", UserFlagStatus.NEW.name(),
                deviceSerialNo);
    }

}
