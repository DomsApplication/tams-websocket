package com.tams.websocket.datasource.entity;

import com.tams.websocket.datasource.entity.enums.UserFlagStatus;
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

@Entity
@Table(name = "device_commands")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
public class DeviceCommands extends BaseEntity {

    @Column(name = "device_serial_no", nullable = false)
    private String deviceSerialNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "get_first_user_data", nullable = false)
    private UserFlagStatus getFirstUserData = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "get_next_user_data", nullable = false)
    private UserFlagStatus getNextUserData = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "remote_enroll", nullable = false)
    private UserFlagStatus remoteEnroll = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "get_department", nullable = false)
    private UserFlagStatus getDepartment = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "set_department", nullable = false)
    private UserFlagStatus setDepartment = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "get_proxy_dept", nullable = false)
    private UserFlagStatus getProxyDept = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "set_proxy_dept", nullable = false)
    private UserFlagStatus setProxyDept = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "get_auto_attendance", nullable = false)
    private UserFlagStatus getAutoAttendance = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "set_auto_attendance", nullable = false)
    private UserFlagStatus setAutoAttendance = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "take_off_manager", nullable = false)
    private UserFlagStatus takeOffManager = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "empty_time_log", nullable = false)
    private UserFlagStatus emptyTimeLog = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "empty_manage_log", nullable = false)
    private UserFlagStatus emptyManageLog = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "empty_all_data", nullable = false)
    private UserFlagStatus emptyAllData = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "empty_user_enrollment_data", nullable = false)
    private UserFlagStatus emptyUserEnrollmentData = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "get_access_time_zone", nullable = false)
    private UserFlagStatus getAccessTimeZone = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "set_access_time_zone", nullable = false)
    private UserFlagStatus setAccessTimeZone = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "get_bell_time", nullable = false)
    private UserFlagStatus getBellTime = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "set_bell_time", nullable = false)
    private UserFlagStatus SetBellTime = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "get_time", nullable = false)
    private UserFlagStatus getTime = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "set_time", nullable = false)
    private UserFlagStatus setTime = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "get_device_status", nullable = false)
    private UserFlagStatus getDeviceStatus = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "get_device_status_all", nullable = false)
    private UserFlagStatus getDeviceStatusAll = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "get_device_info", nullable = false)
    private UserFlagStatus getDeviceInfo = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "get_device_info_all", nullable = false)
    private UserFlagStatus getDeviceInfoAll = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "set_device_info", nullable = false)
    private UserFlagStatus setDeviceInfo = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "enable_device", nullable = false)
    private UserFlagStatus enableDevice = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "lock_control_status", nullable = false)
    private UserFlagStatus lockControlStatus = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "lock_control", nullable = false)
    private UserFlagStatus lockControl = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "get_ethernet_setting", nullable = false)
    private UserFlagStatus getEthernetSetting = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "set_ethernet", nullable = false)
    private UserFlagStatus setEthernet = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "get_wifi_setting", nullable = false)
    private UserFlagStatus getWiFiSetting = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "set_wifi", nullable = false)
    private UserFlagStatus setWiFi = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "get_device_info_ext", nullable = false)
    private UserFlagStatus getDeviceInfoExt = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "set_device_info_ext", nullable = false)
    private UserFlagStatus setDeviceInfoExt = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "get_power_setting", nullable = false)
    private UserFlagStatus getPowerSetting = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "set_power_setting", nullable = false)
    private UserFlagStatus setPowerSetting = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "get_firmware_version", nullable = false)
    private UserFlagStatus getFirmwareVersion = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "firmware_upgrade_http", nullable = false)
    private UserFlagStatus firmwareUpgradeHttp = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "delete_glog_with_pos", nullable = false)
    private UserFlagStatus deleteGlogWithPos = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "get_glog_pos_info", nullable = false)
    private UserFlagStatus getGlogPosInfo = UserFlagStatus.DRAFT;

}
