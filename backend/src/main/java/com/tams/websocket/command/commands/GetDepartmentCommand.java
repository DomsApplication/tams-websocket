package com.tams.websocket.command.commands;

import com.tams.websocket.audit.Trace;
import com.tams.websocket.audit.TraceInfo;
import com.tams.websocket.command.commands.models.GetDepartmentRequest;
import com.tams.websocket.command.commands.models.GetDepartmentResponse;
import com.tams.websocket.datasource.entity.Department;
import com.tams.websocket.datasource.repository.DepartmentRepository;
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
@Component("GetDepartment")
@RequiredArgsConstructor // lombok annotation to generate a constructor with required args
public class GetDepartmentCommand implements Command {

    private GetDepartmentCommand proxy;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void init(Command command) {
        this.proxy = (GetDepartmentCommand) command;
    }

    @Override
    public String sender(TraceInfo traceInfo) {
        log.info("SENDER/GetDepartment:::{}", traceInfo);
        GetDepartmentRequest response = GetDepartmentRequest.builder()
                .request("GetDepartment")
                .deptNo(traceInfo.getDeptId())
                .build();
        try {
            String finalResponse = Utilities.convertToXmlString(response);
            SessionContext.sendResponse(traceInfo.getSessionId(), finalResponse);
            return finalResponse;
        } catch (Exception e) {
            log.error("Error in send response for GetDepartment:", e);
            return "{ \"error\" : " + e.getMessage() + "}";
        }
    }

    @Override
    public void execute(SessionInfo sessionInfo, TraceInfo traceInfo) {
        GetDepartmentResponse response = Utilities.convertToResponseMessage(sessionInfo.getMessageInput(), GetDepartmentResponse.class);
        log.info("Execution:" + response);
        if (response.getResult().equals("OK") || null != response.getResult()) {
            proxy.insertDepartment(response, traceInfo);
        }
    }

    @Trace
    @Transactional
    public Department insertDepartment(GetDepartmentResponse response, TraceInfo traceInfo) {
        Optional<Department> existingDepartment = departmentRepository.findByDepartmentId(response.getDeptNo());

        Department department = null;
        if (existingDepartment.isPresent()) {
            department = existingDepartment.get();
            department.setDepartmentName(Utilities.decodeName(response.getName()));
        } else {
            department = Department.builder()
                    .departmentId(response.getDeptNo())
                    .departmentName(Utilities.decodeName(response.getName()))
                    .build();
        }
        return departmentRepository.save(department);
    }

}
