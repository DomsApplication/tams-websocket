package com.tams.websocket.command.commands;

import com.tams.websocket.audit.Trace;
import com.tams.websocket.audit.TraceInfo;
import com.tams.websocket.command.commands.models.AdminLogRequest;
import com.tams.websocket.command.commands.models.AdminLogResponse;
import com.tams.websocket.datasource.entity.AdminLog;
import com.tams.websocket.datasource.repository.AdminLogRepository;
import com.tams.websocket.socket.SessionContext;
import com.tams.websocket.socket.SessionInfo;
import com.tams.websocket.utils.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component("AdminLog_v2")
@RequiredArgsConstructor // Lombok annotation to generate a constructor with required args
public class AdminLogCommand implements Command {

    private AdminLogCommand proxy;

    @Autowired
    private AdminLogRepository adminLogRepository;

    @Override
    public void init(Command command) {
        this.proxy = (AdminLogCommand) command;
    }

    @Override
    public String sender(TraceInfo traceInfo) {
        log.info("SENDER/AdminLog:::{}", traceInfo);
        return "";
    }

    @Trace
    @Override
    public void execute(SessionInfo sessionInfo, TraceInfo traceInfo) {
        AdminLogRequest request = Utilities.convertToMessage(sessionInfo.getMessageInput(), AdminLogRequest.class);
        log.info("Execution:" + request);
        proxy.createTimeLog(sessionInfo, request, traceInfo);
        proxy.sendResposne(sessionInfo.getSessionId(), request, traceInfo);
    }

    @Trace
    @Transactional
    public AdminLog createTimeLog(SessionInfo sessionInfo, AdminLogRequest request, TraceInfo traceInfo) {
        AdminLog adminLog = AdminLog.builder()
                .terminalType(request.getTerminalType())
                .terminalId(request.getTerminalID())
                .productName(request.getProductName())
                .deviceSerialNo(request.getDeviceSerialNo())
                .deviceUID(request.getDeviceUID())
                .transID(request.getTransID())
                .logID(request.getLogID())
                .time(Utilities.convertStringTimestamp(request.getTime(), Utilities.YYY_MM_DD_T_HH_MM_SS_X))
                .adminID(request.getAdminID())
                .userID(request.getUserID())
                .action(request.getAction())
                .stat(request.getStat())
                .build();
        return adminLogRepository.save(adminLog);
    }

    @Trace
    public String sendResposne(String sessionId, AdminLogRequest request, TraceInfo traceInfo) {
        AdminLogResponse response = AdminLogResponse.builder()
                .response("AdminLog_v2")
                .result("OK")
                .transID(request.getTransID())
                .build();
        try {
            String finalResponse = Utilities.convertToXmlString(response);
            SessionContext.sendResponse(sessionId, finalResponse);
            return finalResponse;
        } catch (Exception e) {
            log.error("Error in send response for ADMIN_LOG:", e);
            return "{ \"error\" : "+e.getMessage()+"}";
        }
    }
}
