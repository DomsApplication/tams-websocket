package com.tams.websocket.command.commands;

import com.tams.websocket.audit.Trace;
import com.tams.websocket.audit.TraceInfo;
import com.tams.websocket.command.commands.models.EnrollFaceByPhotoRequest;
import com.tams.websocket.command.commands.models.EnrollFaceByPhotoResponse;
import com.tams.websocket.datasource.entity.UserInfoExt;
import com.tams.websocket.datasource.repository.UserInfoExtRepository;
import com.tams.websocket.socket.SessionContext;
import com.tams.websocket.socket.SessionInfo;
import com.tams.websocket.utils.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component("EnrollFaceByPhoto")
@RequiredArgsConstructor // lombok annotation to generate a constructor with required args
public class EnrollFaceByPhotoCommand implements Command {

    private EnrollFaceByPhotoCommand proxy;

    @Autowired
    private UserInfoExtRepository userInfoExtRepository;

    @Override
    public void init(Command command) {
        this.proxy = (EnrollFaceByPhotoCommand) command;
    }

    @Override
    public String sender(TraceInfo traceInfo) {
        log.info("SENDER/EnrollFaceByPhoto:::{}", traceInfo);
        Optional<UserInfoExt> existingUserInfoExt = userInfoExtRepository.findByUserId(traceInfo.getUserId());
        if (existingUserInfoExt.isEmpty() || null == existingUserInfoExt.get().getPhotoData()
                || existingUserInfoExt.get().getPhotoData().length == 0) {
            return "{ \"error\" : \"Did not found the User record for the user "+ traceInfo.getUserId()+"\"}";
        }
        UserInfoExt userInfoExt =  existingUserInfoExt.get();
        EnrollFaceByPhotoRequest response = EnrollFaceByPhotoRequest.builder()
                .request("EnrollFaceByPhoto")
                .userID(traceInfo.getUserId())
                .photoSize(Integer.toString(userInfoExt.getPhotoData().length))
                .photoData(Utilities.byteArrayToBase64(userInfoExt.getPhotoData()))
                .build();
        try {
            String finalResponse = Utilities.convertToXmlString(response);
            SessionContext.sendResponse(traceInfo.getSessionId(), finalResponse);
            return finalResponse;
        } catch (Exception e) {
            log.error("Error in send response for EnrollFaceByPhoto:", e);
            return "{ \"error\" : " + e.getMessage() + "}";
        }
    }

    @Override
    public void execute(SessionInfo sessionInfo, TraceInfo traceInfo) {
        EnrollFaceByPhotoResponse response = Utilities.convertToResponseMessage(sessionInfo.getMessageInput(), EnrollFaceByPhotoResponse.class);
        log.info("Execution:" + response);
        proxy.persistResult(response, traceInfo);
    }

    @Trace
    public EnrollFaceByPhotoResponse persistResult(EnrollFaceByPhotoResponse response, TraceInfo traceInfo) {
        return response;
    }

}
