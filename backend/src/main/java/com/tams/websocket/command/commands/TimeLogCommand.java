package com.tams.websocket.command.commands;

import com.tams.websocket.audit.ManvishProcessRawLog;
import com.tams.websocket.audit.Trace;
import com.tams.websocket.audit.TraceInfo;
import com.tams.websocket.command.commands.models.TimeLogRequest;
import com.tams.websocket.command.commands.models.TimeLogResponse;
import com.tams.websocket.datasource.entity.TimeLog;
import com.tams.websocket.datasource.repository.TimeLogRepository;
import com.tams.websocket.socket.SessionContext;
import com.tams.websocket.socket.SessionInfo;
import com.tams.websocket.utils.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component("TimeLog_v2")
@RequiredArgsConstructor // Lombok annotation to generate a constructor with required args
public class TimeLogCommand implements Command {

    private TimeLogCommand proxy;

    @Autowired
    private TimeLogRepository timeLogRepository;

    @Autowired
    private ManvishProcessRawLog processRawLog;

    @Override
    public void init(Command command) {
        this.proxy = (TimeLogCommand) command;
    }

    @Override
    public String sender(TraceInfo traceInfo) { return ""; }

    @Trace
    @Override
    public void execute(SessionInfo sessionInfo, TraceInfo traceInfo) {
        TimeLogRequest request = Utilities.convertToMessage(sessionInfo.getMessageInput(), TimeLogRequest.class);
        log.info("Execution:" + request);
        proxy.createTimeLog(request, traceInfo);
        /** Below code is for migrate Time log into Manvish miFaun Accesslog table */
        //processRawLog.processManvishData(request);
        proxy.sendResposne(sessionInfo.getSessionId(), request, traceInfo);
    }

    @Trace
    @Transactional
    public TimeLog createTimeLog(TimeLogRequest request, TraceInfo traceInfo) {
        TimeLog timeLog = TimeLog.builder()
                .terminalType(request.getTerminalType())
                .terminalId(request.getTerminalID())
                .productName(request.getProductName())
                .deviceSerialNo(request.getDeviceSerialNo())
                .deviceUID(request.getDeviceUID())
                .transID(request.getTransID())
                .logID(request.getLogID())
                .time(Utilities.convertStringTimestamp(request.getTime(), Utilities.YYY_MM_DD_T_HH_MM_SS_X))
                .userID(request.getUserID())
                .action(request.getAction())
                .attendStat(request.getAttendStat())
                .aPStat(request.getAPStat())
                .jobCode(request.getJobCode())
                .photo(request.getPhoto())
                .logImage(request.getLogImage())
                .build();
        return timeLogRepository.save(timeLog);
    }

    @Trace
    public String sendResposne(String sessionId, TimeLogRequest request, TraceInfo traceInfo) {
        TimeLogResponse response = TimeLogResponse.builder()
                .response("TimeLog_v2")
                .result("OK")
                .transID(request.getTransID())
                .build();
        try {
            String finalResponse = Utilities.convertToXmlString(response);
            SessionContext.sendResponse(sessionId, finalResponse);
            return finalResponse;
        } catch (Exception e) {
            log.error("Error in send response for TIME_LOG:", e);
            return "{ \"error\" : "+e.getMessage()+"}";
        }
    }
}
