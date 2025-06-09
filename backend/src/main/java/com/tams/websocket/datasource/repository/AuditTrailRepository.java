package com.tams.websocket.datasource.repository;

import com.tams.websocket.datasource.entity.AuditTrail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditTrailRepository  extends JpaRepository<AuditTrail, String> {

    @Query("SELECT at FROM AuditTrail at WHERE at.traceId = :traceId order by at.startTime asc ")
    List<AuditTrail> findrecordsByTraceId(@Param("traceId") String traceId);

    @Query(value = "SELECT trace_id, MIN(created_on) AS created_on, min(device_serial_no) AS device_serial_no, min(command) AS command, " +
            "SUM(duration) AS duration, min(host_address) AS host_address, min(host_port) AS host_port, MIN(start_time) AS start_time " +
            "FROM audit_trail GROUP BY trace_id " +
            "order by start_time desc " +
            "limit 300 ",
            nativeQuery = true)
    List<Object[]> findAuditTrailsWithAggregation();
}
