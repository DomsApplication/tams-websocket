package com.tams.websocket.command.commands;

import com.tams.websocket.audit.Trace;
import com.tams.websocket.audit.TraceInfo;
import com.tams.websocket.command.commands.models.GetProxyDepartmentRequest;
import com.tams.websocket.command.commands.models.GetProxyDepartmentResponse;
import com.tams.websocket.datasource.entity.ProxyDepartment;
import com.tams.websocket.datasource.repository.ProxyDepartmentRepository;
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
@Component("GetProxyDept")
@RequiredArgsConstructor // lombok annotation to generate a constructor with required args
public class GetProxyDepartmentCommand implements Command {

    private GetProxyDepartmentCommand proxy;

    @Autowired
    private ProxyDepartmentRepository proxyDepartmentRepository;

    @Override
    public void init(Command command) {
        this.proxy = (GetProxyDepartmentCommand) command;
    }

    @Override
    public String sender(TraceInfo traceInfo) {
        log.info("SENDER/GetProxyDept:::{}", traceInfo);
        GetProxyDepartmentRequest response = GetProxyDepartmentRequest.builder()
                .request("GetProxyDept")
                .proxyNo(traceInfo.getDeptId())
                .build();
        try {
            String finalResponse = Utilities.convertToXmlString(response);
            SessionContext.sendResponse(traceInfo.getSessionId(), finalResponse);
            return finalResponse;
        } catch (Exception e) {
            log.error("Error in send response for GetProxyDept:", e);
            return "{ \"error\" : " + e.getMessage() + "}";
        }
    }

    @Override
    public void execute(SessionInfo sessionInfo, TraceInfo traceInfo) {
        GetProxyDepartmentResponse response = Utilities.convertToResponseMessage(sessionInfo.getMessageInput(), GetProxyDepartmentResponse.class);
        log.info("Execution:" + response);
        if (response.getResult().equals("OK") || null != response.getResult()) {
            proxy.insertProxyDepartment(response, traceInfo);
        }
    }

    @Trace
    @Transactional
    public ProxyDepartment insertProxyDepartment(GetProxyDepartmentResponse response, TraceInfo traceInfo) {
        Optional<ProxyDepartment> existingProxyDepartment = proxyDepartmentRepository.findByProxyId(response.getProxyNo());

        ProxyDepartment proxyDepartment = null;
        if (existingProxyDepartment.isPresent()) {
            proxyDepartment = existingProxyDepartment.get();
            proxyDepartment.setProxyName(Utilities.decodeName(response.getName()));
        } else {
            proxyDepartment = ProxyDepartment.builder()
                    .proxyId(response.getProxyNo())
                    .proxyName(Utilities.decodeName(response.getName()))
                    .build();
        }
        return proxyDepartmentRepository.save(proxyDepartment);
    }

}
