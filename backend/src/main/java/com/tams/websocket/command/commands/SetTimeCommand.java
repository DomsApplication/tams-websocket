package com.tams.websocket.command.commands;

import com.tams.websocket.audit.TraceInfo;
import com.tams.websocket.command.commands.models.SetTimeRequest;
import com.tams.websocket.command.commands.models.SetTimeResponse;
import com.tams.websocket.socket.SessionContext;
import com.tams.websocket.socket.SessionInfo;
import com.tams.websocket.utils.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Slf4j
@Component("SetTime")
@RequiredArgsConstructor // lombok annotation to generate a constructor with required args
public class SetTimeCommand implements Command {

    private SetTimeCommand proxy;

    @Override
    public void init(Command command) {
        this.proxy = (SetTimeCommand) command;
    }

    @Override
    public String sender(TraceInfo traceInfo) {
        log.info("SENDER/SetTime:::{}", traceInfo);
        SetTimeRequest request = SetTimeRequest.builder()
                .request("SetTime")
                .time(Utilities.getCurrentTimeStamp(Utilities.YYY_MM_DD_T_HH_MM_SS_X, ZoneId.of("Asia/Kolkata")))
                .build();
        try {
            String finalResponse = Utilities.convertToXmlString(request);
            SessionContext.sendResponse(traceInfo.getSessionId(), finalResponse);
            return finalResponse;
        } catch (Exception e) {
            log.error("Error in send response for SetTime:", e);
            return "{ \"error\" : " + e.getMessage() + "}";
        }
    }

    @Override
    public void execute(SessionInfo sessionInfo, TraceInfo traceInfo) {
        SetTimeResponse response = Utilities.convertToResponseMessage(sessionInfo.getMessageInput(), SetTimeResponse.class);
        log.info("Execution:" + response);
    }
}
