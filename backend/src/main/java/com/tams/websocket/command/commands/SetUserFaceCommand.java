package com.tams.websocket.command.commands;

import com.tams.websocket.audit.Trace;
import com.tams.websocket.audit.TraceInfo;
import com.tams.websocket.command.commands.models.SetUserFaceDataRequest;
import com.tams.websocket.command.commands.models.SetUserFaceDataResponse;
import com.tams.websocket.datasource.entity.UserInfo;
import com.tams.websocket.datasource.entity.UserInfoExt;
import com.tams.websocket.datasource.repository.UserInfoExtRepository;
import com.tams.websocket.datasource.repository.UserInfoRepository;
import com.tams.websocket.socket.SessionContext;
import com.tams.websocket.socket.SessionInfo;
import com.tams.websocket.utils.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component("SetFaceData")
@RequiredArgsConstructor // lombok annotation to generate a constructor with required args
public class SetUserFaceCommand implements Command {

    private SetUserFaceCommand proxy;

    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private UserInfoExtRepository userInfoExtRepository;

    @Override
    public void init(Command command) {
        this.proxy = (SetUserFaceCommand) command;
    }

    @Override
    public String sender(TraceInfo traceInfo) {
        log.info("SENDER/SetFaceData:::{}", traceInfo);
        Optional<UserInfo> existingUserInfo = userInfoRepository.findByUserId(traceInfo.getUserId());
        if (existingUserInfo.isEmpty()) {
            return "{ \"error\" : \"Did not found the User info record for the user "+ traceInfo.getUserId()+"\"}";
        }
        Optional<UserInfoExt> existingUserInfoExt = userInfoExtRepository.findByUserId(traceInfo.getUserId());
        if (existingUserInfoExt.isEmpty() || null == existingUserInfoExt.get().getFaceData()
                || existingUserInfoExt.get().getFaceData().length == 0) {
            return "{ \"error\" : \"Did not found the User face record for the user "+ traceInfo.getUserId()+"\"}";
        }
        UserInfo userInfo =  existingUserInfo.get();
        UserInfoExt userInfoExt =  existingUserInfoExt.get();
        SetUserFaceDataRequest response = SetUserFaceDataRequest.builder()
                .request("SetFaceData")
                .userID(traceInfo.getUserId())
                .privilege(userInfo.getPrivilege())
                .duplicationCheck("Yes")
                .faceData(Utilities.byteArrayToBase64(userInfoExt.getFaceData()))
                .build();
        try {
            String finalResponse = Utilities.convertToXmlString(response);
            SessionContext.sendResponse(traceInfo.getSessionId(), finalResponse);
            return finalResponse;
        } catch (Exception e) {
            log.error("Error in send response for SetUserPhoto:", e);
            return "{ \"error\" : " + e.getMessage() + "}";
        }
    }

    @Override
    public void execute(SessionInfo sessionInfo, TraceInfo traceInfo) {
        SetUserFaceDataResponse response = Utilities.convertToResponseMessage(sessionInfo.getMessageInput(), SetUserFaceDataResponse.class);
        log.info("Execution:" + response);
        proxy.persistResult(response, traceInfo);
    }

    @Trace
    public SetUserFaceDataResponse persistResult(SetUserFaceDataResponse response, TraceInfo traceInfo) {
        return response;
    }

}
