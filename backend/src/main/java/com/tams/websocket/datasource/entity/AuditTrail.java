package com.tams.websocket.datasource.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

@Entity
@Table(name = "audit_trail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
public class AuditTrail extends BaseEntity {

    @Column(name = "trace_id", nullable = false)
    private String traceId;

    @Column(name = "span_id", nullable = false)
    private String spanId;

    @Column(name = "device_serial_no", nullable = false)
    private String deviceSerialNo;

    @Column(name = "host_address")
    private String hostAddress;

    @Column(name = "host_port")
    private String hostPort;

    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @Column(name = "command", nullable = false)
    private String command;

    @Column(name = "method", nullable = false)
    private String method;

    @Column(name = "start_time", nullable = false)
    private long startTime;

    @Column(name = "duration", nullable = false)
    private long duration;

    @Lob
    @Column(name = "input", columnDefinition = "TEXT")
    private String input;

    @Lob
    @Column(name = "output", columnDefinition = "TEXT")
    private String output;

}
