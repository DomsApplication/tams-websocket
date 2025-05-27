package com.tams.websocket.datasource.repository;

import com.tams.websocket.datasource.entity.AdminLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminLogRepository  extends JpaRepository<AdminLog, String> {

    @Query(value = "select al.id, al.device_serial_no, al.user_id,(select ui.name from user_info as ui where ui.user_id = al.user_id) as user_name, " +
            "al.action, al.time as log_time from admin_log al where al.device_serial_no = :deviceSerialNo order by log_time desc " +
            "limit 300 ",
            nativeQuery = true)
    List<Object[]> findAdminLogs(@Param("deviceSerialNo") String deviceSerialNo);

}
