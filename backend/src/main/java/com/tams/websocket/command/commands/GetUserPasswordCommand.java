package com.tams.websocket.command.commands;

import com.tams.websocket.audit.Trace;
import com.tams.websocket.audit.TraceInfo;
import com.tams.websocket.command.commands.models.GetUserPasswordRequest;
import com.tams.websocket.command.commands.models.GetUserPasswordResponse;
import com.tams.websocket.datasource.entity.UserInfoExt;
import com.tams.websocket.datasource.repository.UserInfoExtRepository;
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
@Component("GetUserPassword")
@RequiredArgsConstructor // lombok annotation to generate a constructor with required args
public class GetUserPasswordCommand implements Command {

    private GetUserPasswordCommand proxy;

    @Autowired
    private UserInfoExtRepository userInfoExtRepository;

    @Override
    public void init(Command command) {
        this.proxy = (GetUserPasswordCommand) command;
    }

    @Override
    public String sender(TraceInfo traceInfo) {
        log.info("SENDER/GetUserPassword:::{}", traceInfo);
        GetUserPasswordRequest response = GetUserPasswordRequest.builder()
                .request("GetUserPassword")
                .userID(traceInfo.getUserId())
                .build();
        try {
            String finalResponse = Utilities.convertToXmlString(response);
            SessionContext.sendResponse(traceInfo.getSessionId(), finalResponse);
            return finalResponse;
        } catch (Exception e) {
            log.error("Error in send response for GetUserPassword:", e);
            return "{ \"error\" : " + e.getMessage() + "}";
        }
    }

    @Override
    public void execute(SessionInfo sessionInfo, TraceInfo traceInfo) {
        GetUserPasswordResponse response = Utilities.convertToResponseMessage(sessionInfo.getMessageInput(), GetUserPasswordResponse.class);
        log.info("Execution:" + response);
        if (response.getResult().equals("OK") || null != response.getResult()) {
            proxy.insertUserPhotoData(response, traceInfo);
        }
    }

    @Trace
    @Transactional
    public UserInfoExt insertUserPhotoData(GetUserPasswordResponse response, TraceInfo traceInfo) {
        Optional<UserInfoExt> existingUserInfoExt = userInfoExtRepository.findByUserId(response.getUserID());
        UserInfoExt userInfoExt = null;
        if (existingUserInfoExt.isPresent()) {
            userInfoExt = existingUserInfoExt.get();
            userInfoExt.setPassword(response.getPassword());
        } else {
            userInfoExt = UserInfoExt.builder()
                    .userId(response.getUserID())
                    .card(null)
                    .password(response.getPassword())
                    .faceEnrolled(false)
                    .faceData(null)
                    .photoCaptured(Boolean.TRUE.booleanValue())
                    .photoSize(0)
                    .photoData(null)
                    .timeSet1(null)
                    .timeSet2(null)
                    .timeSet3(null)
                    .timeSet4(null)
                    .timeSet5(null)
                    .build();
        }
        return userInfoExtRepository.save(userInfoExt);
    }

}
