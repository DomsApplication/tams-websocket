package com.tams.websocket.command.commands;

import com.tams.websocket.audit.Trace;
import com.tams.websocket.audit.TraceInfo;
import com.tams.websocket.command.commands.models.GetUserCardNoRequest;
import com.tams.websocket.command.commands.models.GetUserCardNoResponse;
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
@Component("GetUserCardNo")
@RequiredArgsConstructor // lombok annotation to generate a constructor with required args
public class GetUserCardNoCommand implements Command {

    private GetUserCardNoCommand proxy;

    @Autowired
    private UserInfoExtRepository userInfoExtRepository;

    @Override
    public void init(Command command) {
        this.proxy = (GetUserCardNoCommand) command;
    }

    @Override
    public String sender(TraceInfo traceInfo) {
        log.info("SENDER/GetUserCardNo:::{}", traceInfo);
        GetUserCardNoRequest response = GetUserCardNoRequest.builder()
                .request("GetUserCardNo")
                .userID(traceInfo.getUserId()).build();
        try {
            String finalResponse = Utilities.convertToXmlString(response);
            SessionContext.sendResponse(traceInfo.getSessionId(), finalResponse);
            return finalResponse;
        } catch (Exception e) {
            log.error("Error in send response for GetUserCardNo:", e);
            return "{ \"error\" : " + e.getMessage() + "}";
        }
    }

    @Override
    public void execute(SessionInfo sessionInfo, TraceInfo traceInfo) {
        GetUserCardNoResponse response = Utilities.convertToResponseMessage(sessionInfo.getMessageInput(), GetUserCardNoResponse.class);
        log.info("Execution:" + response);
        if (response.getResult().equals("OK") || null != response.getResult()) {
            proxy.insertUserPhotoData(response, traceInfo);
        }
    }

    @Trace
    @Transactional
    public UserInfoExt insertUserPhotoData(GetUserCardNoResponse response, TraceInfo traceInfo) {
        Optional<UserInfoExt> existingUserInfoExt = userInfoExtRepository.findByUserId(response.getUserID());
        UserInfoExt userInfoExt = null;
        if (existingUserInfoExt.isPresent()) {
            userInfoExt = existingUserInfoExt.get();
            userInfoExt.setCard(Utilities.decodeBase64String(response.getCardNo()));
        } else {
            userInfoExt = UserInfoExt.builder()
                    .userId(response.getUserID())
                    .card(Utilities.decodeBase64String(response.getCardNo()))
                    .password(null)
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
