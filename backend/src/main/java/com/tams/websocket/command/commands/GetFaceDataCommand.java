package com.tams.websocket.command.commands;

import com.tams.websocket.audit.Trace;
import com.tams.websocket.audit.TraceInfo;
import com.tams.websocket.command.commands.models.GetUserFaceResponse;
import com.tams.websocket.command.commands.models.GetUserPhotoRequest;
import com.tams.websocket.datasource.entity.UserInfoExt;
import com.tams.websocket.datasource.repository.UserInfoExtRepository;
import com.tams.websocket.socket.SessionContext;
import com.tams.websocket.socket.SessionInfo;
import com.tams.websocket.utils.Utilities;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component("GetFaceData")
@RequiredArgsConstructor // lombok annotation to generate a constructor with required args
public class GetFaceDataCommand implements Command {

    private GetFaceDataCommand proxy;

    @Autowired
    private UserInfoExtRepository userInfoExtRepository;

    @Override
    public void init(Command command) {
        this.proxy = (GetFaceDataCommand) command;
    }

    @Override
    public String sender(TraceInfo traceInfo) {
        log.info("SENDER/GetFaceData:::{}", traceInfo);
        GetUserPhotoRequest response = GetUserPhotoRequest.builder()
                .request("GetFaceData")
                .userID(traceInfo.getUserId())
                .build();
        try {
            String finalResponse = Utilities.convertToXmlString(response);
            SessionContext.sendResponse(traceInfo.getSessionId(), finalResponse);
            return finalResponse;
        } catch (Exception e) {
            log.error("Error in send response for GetFaceData:", e);
            return "{ \"error\" : " + e.getMessage() + "}";
        }
    }

    @Override
    public void execute(SessionInfo sessionInfo, TraceInfo traceInfo) {
        GetUserFaceResponse response = Utilities.convertToResponseMessage(sessionInfo.getMessageInput(), GetUserFaceResponse.class);
        log.info("Execution:" + response);
        if (response.getResult().equals("OK") || null != response.getResult()) {
            proxy.insertUserFaceData(response, traceInfo);
        }
    }

    @Trace
    @Transactional
    public UserInfoExt insertUserFaceData(GetUserFaceResponse response, TraceInfo traceInfo) {
        Optional<UserInfoExt> existingUserInfoExt = userInfoExtRepository.findByUserId(response.getUserID());
        byte[] binary_data = Utilities.convertBase64Binary(response.getFaceData());
        UserInfoExt userInfoExt = null;
        if (existingUserInfoExt.isPresent()) {
            userInfoExt = existingUserInfoExt.get();
            userInfoExt.setFaceEnrolled(Utilities.convertToBoolean(response.getFaceEnrolled()));
            userInfoExt.setFaceData(binary_data);
        } else {
            userInfoExt = UserInfoExt.builder()
                    .userId(response.getUserID())
                    .card(null)
                    .password(null)
                    .faceEnrolled(Utilities.convertToBoolean(response.getFaceEnrolled()))
                    .faceData(binary_data)
                    .photoCaptured(Boolean.FALSE.booleanValue())
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
