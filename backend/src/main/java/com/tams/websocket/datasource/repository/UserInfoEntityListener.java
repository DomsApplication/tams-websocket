package com.tams.websocket.datasource.repository;

import com.tams.websocket.datasource.DeviceUserIdGenerator;
import com.tams.websocket.datasource.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.PrePersist;

@Component
public class UserInfoEntityListener {

    private static DeviceUserIdGenerator generator;

    @Autowired
    public void init(DeviceUserIdGenerator injectedGenerator) {
        UserInfoEntityListener.generator = injectedGenerator;
    }

    @PrePersist
    public void setDeviceUserId(UserInfo userInfo) {
        if (userInfo.getDeviceUserId() == null) {
            Long newId = generator.getNextId();
            userInfo.setDeviceUserId(newId);
        }
    }
}

