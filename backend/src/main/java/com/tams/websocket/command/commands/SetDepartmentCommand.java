package com.tams.websocket.command.commands;

import com.tams.websocket.audit.Trace;
import com.tams.websocket.audit.TraceInfo;
import com.tams.websocket.command.commands.models.SetDepartmentRequest;
import com.tams.websocket.command.commands.models.SetDepartmentResponse;
import com.tams.websocket.datasource.entity.Department;
import com.tams.websocket.datasource.repository.DepartmentRepository;
import com.tams.websocket.socket.SessionContext;
import com.tams.websocket.socket.SessionInfo;
import com.tams.websocket.utils.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component("SetDepartment")
@RequiredArgsConstructor // lombok annotation to generate a constructor with required args
public class SetDepartmentCommand implements Command {

    private SetDepartmentCommand proxy;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void init(Command command) {
        this.proxy = (SetDepartmentCommand) command;
    }

    @Override
    public String sender(TraceInfo traceInfo) {
        log.info("SENDER/SetDepartment:::{}", traceInfo);
        Optional<Department> existingDepartment = departmentRepository.findByDepartmentId(traceInfo.getDeptId());
        if (existingDepartment.isEmpty() || null == existingDepartment.get().getDepartmentName()) {
            return "{ \"error\" : \"Did not found the Department record for the DeptID " + traceInfo.getDeptId() + "\"}";
        }
        Department department = existingDepartment.get();
        SetDepartmentRequest response = SetDepartmentRequest.builder()
                .request("SetDepartment")
                .deptNo(department.getDepartmentId())
                .data(department.getDepartmentName())
                .build();
        try {
            String finalResponse = Utilities.convertToXmlString(response);
            SessionContext.sendResponse(traceInfo.getSessionId(), finalResponse);
            return finalResponse;
        } catch (Exception e) {
            log.error("Error in send response for SetDepartment:", e);
            return "{ \"error\" : " + e.getMessage() + "}";
        }
    }

    @Override
    public void execute(SessionInfo sessionInfo, TraceInfo traceInfo) {
        SetDepartmentResponse response = Utilities.convertToResponseMessage(sessionInfo.getMessageInput(), SetDepartmentResponse.class);
        log.info("Execution:" + response);
        proxy.persistResult(response, traceInfo);
    }

    @Trace
    public SetDepartmentResponse persistResult(SetDepartmentResponse response, TraceInfo traceInfo) {
        return response;
    }

}