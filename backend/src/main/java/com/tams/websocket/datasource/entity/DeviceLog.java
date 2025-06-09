package com.tams.websocket.datasource.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "device_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
public class DeviceLog extends BaseEntity {

    @Column(name = "terminal_type", nullable = false)
    private String terminalType;

    @Column(name = "terminal_id", nullable = false)
    private String terminalId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "device_serial_no", nullable = false)
    private String deviceSerialNo;

    @Column(name = "device_uid", nullable = false)
    private String deviceUID;

    @Column(name = "host_address", nullable = false)
    private String hostAddress;

    @Column(name = "host_port", nullable = false)
    private String hostPort;

    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @Column(name = "device_time", nullable = false)
    private LocalDateTime deviceTime;

    @Column(name = "server_time", nullable = false)
    private LocalDateTime serverTime;

}
