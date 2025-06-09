package com.tams.websocket.datasource.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "time_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
public class TimeLog extends BaseEntity {

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

    @Column(name = "trans_id", nullable = false)
    private String transID;

    @Column(name = "log_id", nullable = false)
    private String logID;

    @Column(name = "Time", nullable = false)
    private LocalDateTime time;

    @Column(name = "user_id", nullable = false)
    private String userID;

    @Column(name = "action")
    private String action;

    @Column(name = "attend_stat")
    private String attendStat;

    @Column(name = "ap_stat")
    private String aPStat;

    @Column(name = "job_code")
    private String jobCode;

    @Column(name = "photo")
    private String photo;

    @Lob
    @Column(name = "log_image", columnDefinition = "TEXT")
    private String logImage;

}
