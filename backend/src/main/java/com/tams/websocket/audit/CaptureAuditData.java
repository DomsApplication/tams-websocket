package com.tams.websocket.audit;

import com.tams.websocket.datasource.entity.AuditTrail;
import com.tams.websocket.datasource.repository.AuditTrailRepository;
import com.tams.websocket.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CaptureAuditData {

    @Autowired
    private AuditTrailRepository auditTrailRepository;

    @Transactional
    @Async("taskExecutor")
    public void captureAuditDataInDatabase(TraceInfo traceInfo, String fullQualifiedMethodName, long startTime,
                                           long  executionTime, String input, String output) throws  Exception {

        AuditTrail trail = AuditTrail.builder()
                .traceId(traceInfo.getTraceId())
                .spanId(Utilities.generateUUID4())
                .deviceSerialNo(traceInfo.getDeviceSerialNo())
                .hostAddress(traceInfo.getHostAddress())
                .hostPort(traceInfo.getHostPort())
                .sessionId(traceInfo.getSessionId())
                .command(traceInfo.getCommand())
                .method(fullQualifiedMethodName)
                .startTime(startTime)
                .duration(executionTime)
                .input(input)
                .output(output)
                .build();
        auditTrailRepository.save(trail);
    }
}
