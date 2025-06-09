package com.tams.websocket.datasource.entity;

import com.tams.websocket.datasource.entity.enums.DeviceStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "device_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
public class DeviceInfo extends BaseEntity {

    @Column(name = "terminal_type", nullable = false)
    private String terminalType;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "device_serial_no", nullable = false)
    private String deviceSerialNo;

    @Column(name = "cloud_id", nullable = false)
    private String cloudId;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "host_address", nullable = false)
    private String hostAddress;

    @Column(name = "host_port", nullable = false)
    private String hostPort;

    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeviceStatus status;

    @Column(name = "registered_on")
    private LocalDateTime registeredOn;

    @Column(name = "logged_on")
    private LocalDateTime loggedOn;

    @Column(name = "last_connected_time")
    private LocalDateTime lastConnectedTime;

}
