package com.tams.websocket.command;

import com.tams.websocket.datasource.entity.enums.UserFlagStatus;
import com.tams.websocket.datasource.repository.DeviceCommandsRepositoryCustom;
import com.tams.websocket.datasource.repository.UserCommandsRepositoryCustom;
import com.tams.websocket.utils.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnyUpdate {

    @Autowired
    private UserCommandsRepositoryCustom userCommandsRepositoryCustom;

    @Autowired
    private DeviceCommandsRepositoryCustom deviceCommandsRepositoryCustom;

    @Autowired
    private SenderHandler senderHandler;

    @Async("taskExecutor")
    public void checkAnyUpdateForDevice(String deviceSerialNo, String sessionId) {
        /** User command flag */
        try {
            List<Map<String, Object>> usersflagData = userCommandsRepositoryCustom
                    .findUserCommandFlagWithNewStatus(deviceSerialNo, 5);
            for (Map<String, Object> row : usersflagData) {
                String device_serial_no = row.get("device_serial_no").toString();
                String user_id = row.get("user_id").toString();

                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue().toString();
                    if (key.equals("device_serial_no") || key.equals("user_id")) {
                        continue;
                    }
                    if (UserFlagStatus.NEW.name().equals(value)) {
                        /* Update Command Flag to 'QUEUE'*/
                        userCommandsRepositoryCustom.updateSingleUserData(key, UserFlagStatus.QUEUE.name(),
                                device_serial_no, user_id);
                        senderHandler.sendRequest(device_serial_no, sessionId, user_id,
                                Utilities.capitalizeFirstLetter(key), "UserType");
                    }
                }
            }

        } catch (Exception e) {
            log.error("Error during async execution", e);
        }

        /** Device command flag */
        try {
            List<Map<String, Object>> deviceflagData = deviceCommandsRepositoryCustom
                    .findDeviceCommandFlagWithNewStatus(deviceSerialNo, 5);
            for (Map<String, Object> row : deviceflagData) {
                String device_serial_no = row.get("device_serial_no").toString();

                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue().toString();
                    if (key.equals("device_serial_no")) {
                        continue;
                    }
                    if (UserFlagStatus.NEW.name().equals(value)) {
                        /* Update Command Flag to 'QUEUE'*/
                        deviceCommandsRepositoryCustom.updateSingleDeviceData(key, UserFlagStatus.QUEUE.name(),
                                device_serial_no);

                        senderHandler.sendRequest(device_serial_no, sessionId, "",
                                Utilities.capitalizeFirstLetter(key), "DeviceType");
                    }
                }
            }

        } catch (Exception e) {
            log.error("Error during async execution", e);
        }
    }
}
