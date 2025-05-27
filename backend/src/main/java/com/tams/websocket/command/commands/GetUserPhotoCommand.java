package com.tams.websocket.command.commands;

import com.tams.websocket.audit.Trace;
import com.tams.websocket.audit.TraceInfo;
import com.tams.websocket.command.commands.models.GetUserPhotoRequest;
import com.tams.websocket.command.commands.models.GetUserPhotoResponse;
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
@Component("GetUserPhoto")
@RequiredArgsConstructor // lombok annotation to generate a constructor with required args
public class GetUserPhotoCommand implements Command {

    private GetUserPhotoCommand proxy;

    @Autowired
    private UserInfoExtRepository userInfoExtRepository;

    @Override
    public void init(Command command) {
        this.proxy = (GetUserPhotoCommand) command;
    }

    @Override
    public String sender(TraceInfo traceInfo) {
        log.info("SENDER/GetUserPhoto:::{}", traceInfo);
        GetUserPhotoRequest response = GetUserPhotoRequest.builder()
                .request("GetUserPhoto")
                .userID(traceInfo.getUserId()).build();
        try {
            //String finalResponse = Utilities.convertToXmlString(response);
            String finalResponse = "<?xml version=\"1.0\"?><Message><Request>GetUserPhoto</Request><UserID>4920</UserID></Message>";

            SessionContext.sendResponse(traceInfo.getSessionId(), finalResponse);
            log.info("SENDER/GetUserPhoto/1:::{}", finalResponse);
            return finalResponse;
        } catch (Exception e) {
            log.error("Error in send response for LOGIN:", e);
            return "{ \"error\" : " + e.getMessage() + "}";
        }
    }

    @Override
    public void execute(SessionInfo sessionInfo, TraceInfo traceInfo) {
        GetUserPhotoResponse response = Utilities.convertToResponseMessage(sessionInfo.getMessageInput(), GetUserPhotoResponse.class);
        log.info("Execution:" + response);
        if (response.getResult().equals("OK") || null != response.getResult()) {
            proxy.insertUserPhotoData(response, traceInfo);
        }
    }

    @Trace
    @Transactional
    public UserInfoExt insertUserPhotoData(GetUserPhotoResponse response, TraceInfo traceInfo) {
        Optional<UserInfoExt> existingUserInfoExt = userInfoExtRepository.findByUserId(response.getUserID());
        byte[] binary_data = Utilities.convertBase64Binary(response.getPhotoData());
        UserInfoExt userInfoExt = null;
        if (existingUserInfoExt.isPresent()) {
            userInfoExt = existingUserInfoExt.get();
            userInfoExt.setPhotoCaptured(true);
            userInfoExt.setPhotoSize(binary_data.length);
            userInfoExt.setPhotoData(binary_data);
        } else {
            userInfoExt = UserInfoExt.builder()
                    .userId(response.getUserID())
                    .card(null)
                    .password(null)
                    .faceEnrolled(false)
                    .faceData(null)
                    .photoCaptured(true)
                    .photoSize(binary_data.length)
                    .photoData(binary_data)
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
