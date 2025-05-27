package com.tams.websocket.datasource;

import com.tams.websocket.datasource.entity.Counter;
import com.tams.websocket.datasource.repository.CounterRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceUserIdGenerator {
    @Autowired
    private CounterRepository counterRepository;

    @Transactional
    public synchronized Long getNextId() {
        Counter counter = counterRepository.findById("device_user_id")
                .orElseGet(() -> new Counter("device_user_id", 1L));

        Long currentValue = counter.getValue();
        counter.setValue(currentValue + 1);
        counterRepository.save(counter);
        return currentValue;
    }
}
