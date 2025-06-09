package com.tams.websocket.command.commands;

import com.tams.websocket.audit.Trace;
import com.tams.websocket.audit.TraceInfo;
import com.tams.websocket.command.commands.models.EmptyManageLogRequest;
import com.tams.websocket.command.commands.models.EmptyManageLogResponse;
import com.tams.websocket.socket.SessionContext;
import com.tams.websocket.socket.SessionInfo;
import com.tams.websocket.utils.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("EmptyManageLog")
@RequiredArgsConstructor // lombok annotation to generate a constructor with required args
public class EmptyManageLogCommand implements Command {

    private EmptyManageLogCommand proxy;

    @Override
    public void init(Command command) {
        this.proxy = (EmptyManageLogCommand) command;
    }

    @Trace
    @Override
    public String sender(TraceInfo traceInfo) {
        log.info("SENDER/EmptyManageLog:::{}", traceInfo);
        EmptyManageLogRequest response = EmptyManageLogRequest.builder()
                .request("EmptyManageLog")
                .build();
        try {
            String finalResponse = Utilities.convertToXmlString(response);
            SessionContext.sendResponse(traceInfo.getSessionId(), finalResponse);
            return finalResponse;
        } catch (Exception e) {
            log.error("Error in send response for EmptyManageLog:", e);
            return "{ \"error\" : " + e.getMessage() + "}";
        }
    }

    @Trace
    @Override
    public void execute(SessionInfo sessionInfo, TraceInfo traceInfo) {
        EmptyManageLogResponse response = Utilities.convertToResponseMessage(sessionInfo.getMessageInput(), EmptyManageLogResponse.class);
        log.info("Execution:" + response);
        proxy.persistResult(response, traceInfo);
    }

    @Trace
    public EmptyManageLogResponse persistResult(EmptyManageLogResponse response, TraceInfo traceInfo) {
        return response;
    }
}
