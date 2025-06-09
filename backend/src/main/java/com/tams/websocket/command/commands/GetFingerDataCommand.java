package com.tams.websocket.command.commands;

import com.tams.websocket.audit.Trace;
import com.tams.websocket.audit.TraceInfo;
import com.tams.websocket.command.commands.models.GetUserFingerRequest;
import com.tams.websocket.command.commands.models.GetUserFingerResponse;
import com.tams.websocket.datasource.entity.UserFingers;
import com.tams.websocket.datasource.repository.UserFingersRepository;
import com.tams.websocket.socket.SessionContext;
import com.tams.websocket.socket.SessionInfo;
import com.tams.websocket.utils.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component("GetFingerData")
@RequiredArgsConstructor // lombok annotation to generate a constructor with required args
public class GetFingerDataCommand implements Command {

    private GetFingerDataCommand proxy;

    @Autowired
    private UserFingersRepository userFingersRepository;

    @Override
    public void init(Command command) {
        this.proxy = (GetFingerDataCommand) command;
    }

    @Override
    public String sender(TraceInfo traceInfo) {
        log.info("SENDER/GetFingerData:::{}", traceInfo);
        try {
            List<UserFingers> userFingers = userFingersRepository.findByUserId(traceInfo.getUserId());
            String fingerOnly = "1";
            List<String> returnUserFingers = new ArrayList<>();
            for (UserFingers userFinger : userFingers) {
                GetUserFingerRequest response = GetUserFingerRequest.builder()
                        .request("GetFingerData")
                        .userID(traceInfo.getUserId())
                        .fingerNo(userFinger.getFingerId().toString())
                        .fingerOnly(fingerOnly)
                        .build();
                String finalResponse = Utilities.convertToXmlString(response);
                returnUserFingers.add(finalResponse);
                SessionContext.sendResponse(traceInfo.getSessionId(), finalResponse);
            }
            return String.join(", ", returnUserFingers);
        } catch (Exception e) {
            log.error("Error in send response for LOGIN:", e);
            return "{ \"error\" : " + e.getMessage() + "}";
        }
    }


    @Override
    public void execute(SessionInfo sessionInfo, TraceInfo traceInfo) {
        GetUserFingerResponse response = Utilities.convertToResponseMessage(sessionInfo.getMessageInput(),
                GetUserFingerResponse.class);
        log.info("Execution:" + response);
        if (null == response.getResult() || (null != response.getResult() && response.getResult().equals("OK"))) {
            proxy.setUserFingers(response, traceInfo);
        }
    }

    @Trace
    @Transactional
    public void setUserFingers(GetUserFingerResponse response, TraceInfo traceInfo) {
        // Check if the record exists
        Optional<UserFingers> existingUserFingers = userFingersRepository
                .findByUserIdAndFingerId(response.getUserID(), Integer.valueOf(response.getFingerNo()));
        byte[] binary_data = (null == response.getFingerData()) ? null : Utilities.convertBase64Binary(response.getFingerData());
        UserFingers userFingers = null;
        if (existingUserFingers.isPresent()) {
            userFingers = existingUserFingers.get();
            userFingers.setFingerData(binary_data);
        } else {
            userFingers = UserFingers.builder()
                    .userId(response.getUserID())
                    .fingerId(Integer.valueOf(response.getFingerNo()))
                    .enrolled(true)
                    .duress(Utilities.convertToBoolean(response.getDuress()))
                    .fingerData(binary_data)
                    .build();
        }
        userFingersRepository.save(userFingers);
    }

}
