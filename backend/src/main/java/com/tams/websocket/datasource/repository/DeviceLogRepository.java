package com.tams.websocket.datasource.repository;


import com.tams.websocket.datasource.entity.DeviceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeviceLogRepository extends JpaRepository<DeviceLog, String> {

    @Query("SELECT d FROM DeviceLog d WHERE d.deviceSerialNo = :deviceSerialNo AND d.deviceTime BETWEEN :start AND :end ORDER BY d.deviceTime ASC ")
    List<DeviceLog> findDeviceLogsWithinPeriod(@Param("deviceSerialNo") String deviceSerialNo,
                                               @Param("start") LocalDateTime start,
                                               @Param("end") LocalDateTime end);


    @Query(value = "SELECT DISTINCT device_serial_no, device_time, product_name, created_on FROM device_log " +
            "WHERE created_on >= NOW() - INTERVAL 30 MINUTE ORDER BY created_on DESC limit 10 ",
            nativeQuery = true)
    List<Object[]> getDashboardDeviceLogTableData();


}
