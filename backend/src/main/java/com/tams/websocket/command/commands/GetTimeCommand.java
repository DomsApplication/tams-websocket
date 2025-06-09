package com.tams.websocket.command.commands;

import com.tams.websocket.audit.TraceInfo;
import com.tams.websocket.command.commands.models.GetTimeRequest;
import com.tams.websocket.command.commands.models.GetTimeResponse;
import com.tams.websocket.socket.SessionContext;
import com.tams.websocket.socket.SessionInfo;
import com.tams.websocket.utils.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("GetTime")
@RequiredArgsConstructor // lombok annotation to generate a constructor with required args
public class GetTimeCommand implements Command {

    private GetTimeCommand proxy;

    @Override
    public void init(Command command) {
        this.proxy = (GetTimeCommand) command;
    }

    @Override
    public String sender(TraceInfo traceInfo) {
        log.info("SENDER/GetTime:::{}", traceInfo);
        GetTimeRequest response = GetTimeRequest.builder()
                .request("GetTime")
                .build();
        try {
            String finalResponse = Utilities.convertToXmlString(response);
            SessionContext.sendResponse(traceInfo.getSessionId(), finalResponse);
            return finalResponse;
        } catch (Exception e) {
            log.error("Error in send response for GetTime:", e);
            return "{ \"error\" : " + e.getMessage() + "}";
        }
    }

    @Override
    public void execute(SessionInfo sessionInfo, TraceInfo traceInfo) {
        GetTimeResponse response = Utilities.convertToResponseMessage(sessionInfo.getMessageInput(), GetTimeResponse.class);
        log.info("Execution:" + response);
    }
}
