package com.tams.websocket.datasource.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "acslog_rawdata")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ManvishAcsLogRawData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Use IDENTITY for MySQL auto_increment
    @Column(name = "pkacslog", updatable = false, nullable = false)
    private int pkacsLog;

    @Column(name = "unitid")
    private String unitId;

    @Column(name = "acsdate")
    private LocalDateTime acsDate;

    @Column(name = "deptid")
    private String deptId;

    @Column(name = "empid")
    private String empId;

    @Column(name = "acstime")
    private LocalDateTime acsTime;

    @Column(name = "inoutmode")
    private String inoutMode;

    @Column(name = "emptype")
    private String empType;

    @Column(name = "compid")
    private int compId;

    @Column(name = "empmode")
    private int empMode;

    @Column(name = "DELETEDFLAG", length = 1, nullable = false)
    private String DELETEDFLAG;

    @Column(name = "processflag", length = 1, nullable = false)
    private String processflag;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "modifieddate")
    private LocalDateTime modifieddate;

}
