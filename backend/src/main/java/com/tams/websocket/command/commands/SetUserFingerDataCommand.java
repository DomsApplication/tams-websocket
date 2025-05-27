package com.tams.websocket.command.commands;

import com.tams.websocket.audit.Trace;
import com.tams.websocket.audit.TraceInfo;
import com.tams.websocket.command.commands.models.SetUserFingerDataRequest;
import com.tams.websocket.command.commands.models.SetUserFingerDataResponse;
import com.tams.websocket.datasource.entity.UserFingers;
import com.tams.websocket.datasource.entity.UserInfo;
import com.tams.websocket.datasource.repository.UserFingersRepository;
import com.tams.websocket.datasource.repository.UserInfoRepository;
import com.tams.websocket.socket.SessionContext;
import com.tams.websocket.socket.SessionInfo;
import com.tams.websocket.utils.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component("SetFingerData")
@RequiredArgsConstructor // lombok annotation to generate a constructor with required args
public class SetUserFingerDataCommand implements Command {

    private SetUserFingerDataCommand proxy;

    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private UserFingersRepository userFingersRepository;

    @Override
    public void init(Command command) {
        this.proxy = (SetUserFingerDataCommand) command;
    }

    @Override
    public String sender(TraceInfo traceInfo) {
        log.info("SENDER/SetFingerData:::{}", traceInfo);
        List<String> returnUserFingers = new ArrayList<>();
        try {
            Optional<UserInfo> existingUserInfo = userInfoRepository.findByUserId(traceInfo.getUserId());
            if (existingUserInfo.isEmpty()) {
                return "{ \"error\" : \"Did not found the User info record for the user " + traceInfo.getUserId() + "\"}";
            }

            List<UserFingers> userFingers = userFingersRepository.findEnrolledUserFingersWithFingerData(traceInfo.getUserId());
            if (userFingers.isEmpty()) {
                return "{ \"error\" : \"Did not found the User fingers records for the user " + traceInfo.getUserId() + "\"}";
            }
            UserInfo userInfo = existingUserInfo.get();

            for (UserFingers userFinger : userFingers) {
                SetUserFingerDataRequest response = SetUserFingerDataRequest.builder()
                        .request("SetFingerData")
                        .userID(traceInfo.getUserId())
                        .privilege(userInfo.getPrivilege())
                        .fingerNo(userFinger.getFingerId().toString())
                        .duplicationCheck("1")
                        .duress(userFinger.getDuress().toString())
                        .fingerData(Utilities.byteArrayToBase64(userFinger.getFingerData()))
                        .build();
                String finalResponse = Utilities.convertToXmlString(response);
                SessionContext.sendResponse(traceInfo.getSessionId(), finalResponse);
                returnUserFingers.add(finalResponse);
            }
        } catch (Exception e) {
            log.error("Error in send response for SetFingerData:", e);
            return "{ \"error\" : " + e.getMessage() + "}";
        }
        return String.join(", ", returnUserFingers);
    }

    @Override
    public void execute(SessionInfo sessionInfo, TraceInfo traceInfo) {
        SetUserFingerDataResponse response = Utilities.convertToResponseMessage(sessionInfo.getMessageInput(),
                SetUserFingerDataResponse.class);
        log.info("Execution:" + response);
        proxy.persistResult(response, traceInfo);
    }

    @Trace
    public SetUserFingerDataResponse persistResult(SetUserFingerDataResponse response, TraceInfo traceInfo) {
        return response;
    }

}
