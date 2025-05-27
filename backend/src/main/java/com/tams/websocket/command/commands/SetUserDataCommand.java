package com.tams.websocket.command.commands;

import com.tams.websocket.audit.Trace;
import com.tams.websocket.audit.TraceInfo;
import com.tams.websocket.command.commands.models.SetUserDataResponse;
import com.tams.websocket.command.commands.models.SetUserDataRequest;
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
@Component("SetUserData")
@RequiredArgsConstructor // lombok annotation to generate a constructor with required args
public class SetUserDataCommand implements Command {

    private SetUserDataCommand proxy;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserInfoExtRepository userInfoExtRepository;

    @Override
    public void init(Command command) {
        this.proxy = (SetUserDataCommand) command;
    }

    @Override
    public String sender(TraceInfo traceInfo) {
        log.info("SetUserData/SENDER:::{}", traceInfo);
        Optional<UserInfo> existingUserInfo = userInfoRepository.findByUserId(traceInfo.getUserId());
        if (existingUserInfo.isEmpty()) {
            return "{ \"error\" : \"user info not found for the userID: "+traceInfo.getUserId()+"\"}";
        }
        Optional<UserInfoExt> existingUserExtInfo = userInfoExtRepository.findByUserId(traceInfo.getUserId());
        if (existingUserExtInfo.isEmpty()) {
            return "{ \"error\" : \"user info Ext not found for the userID: "+traceInfo.getUserId()+"\"}";
        }
        UserInfo userInfo = existingUserInfo.get();
        UserInfoExt userInfoExt = existingUserExtInfo.get();

        SetUserDataRequest request = SetUserDataRequest.builder()
                .request("SetUserData")
                .userID(userInfo.getUserId())
                .type("Set")
                .name(Utilities.encodeName(userInfo.getName()))
                .privilege(userInfo.getPrivilege())
                .enabled((userInfo.getEnabled() ? "Yes" : "No"))
                .timeSet1(userInfoExt.getTimeSet1())
                .timeSet2(userInfoExt.getTimeSet2())
                .timeSet3(userInfoExt.getTimeSet3())
                .timeSet4(userInfoExt.getTimeSet4())
                .timeSet5(userInfoExt.getTimeSet5())
                .userPeriodUsed((userInfo.getUserPeriodUsed() ? "Yes" : "No"))
                .userPeriodStart(Utilities.formatLocalDateTimeToUTCString(userInfo.getUserPeriodStart(), Utilities.YYY_MM_DD_T_HH_MM_SS_X))
                .userPeriodEnd(Utilities.formatLocalDateTimeToUTCString(userInfo.getUserPeriodEnd(), Utilities.YYY_MM_DD_T_HH_MM_SS_X))
                .card(Utilities.encodeBase64String(userInfoExt.getCard()))
                .pwd(userInfoExt.getPassword())
                .faceData(null)
                .allowNoCertificate("Yes")
                .build();
        try {
            String finalResponse = Utilities.convertToXmlString(request);
            SessionContext.sendResponse(traceInfo.getSessionId(), finalResponse);
            return finalResponse;
        } catch (Exception e) {
            log.error("Error in send response for SetUserData:", e);
            return "{ \"error\" : " + e.getMessage() + "}";
        }
    }

    @Override
    public void execute(SessionInfo sessionInfo, TraceInfo traceInfo) {
        SetUserDataResponse response = Utilities.convertToResponseMessage(sessionInfo.getMessageInput(), SetUserDataResponse.class);
        log.info("Execution:" + response);
        proxy.persistResult(response, traceInfo);
    }

    @Trace
    public SetUserDataResponse persistResult(SetUserDataResponse response, TraceInfo traceInfo) {
        return response;
    }

}
