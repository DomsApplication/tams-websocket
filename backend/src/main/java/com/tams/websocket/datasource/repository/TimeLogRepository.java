package com.tams.websocket.datasource.repository;

import com.tams.websocket.datasource.entity.TimeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeLogRepository  extends JpaRepository<TimeLog, String> {

    @Query(value = "select tl.id, tl.device_serial_no, tl.user_id,(select ui.name from user_info as ui where ui.user_id = tl.user_id) as user_name,  " +
            "tl.attend_stat, tl.action, tl.time as log_time from time_log tl where tl.device_serial_no = :deviceSerialNo order by log_time desc " +
            "limit 300 ",
            nativeQuery = true)
    List<Object[]> findTimeLogs(@Param("deviceSerialNo") String deviceSerialNo);

    @Query(value = "select IF(device_serial_no IS NULL OR device_serial_no = '', '-', device_serial_no) AS device_serial_no, " +
            "user_id, time from time_log WHERE created_on >= NOW() - INTERVAL 30 MINUTE order by time desc limit 10 ",
            nativeQuery = true)
    List<Object[]> getDashboardTimeLogTableData();


}
