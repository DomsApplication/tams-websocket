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
@Table(name = "user_commands")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
public class UserCommands extends BaseEntity {

    @Column(name = "device_serial_no", nullable = false)
    private String deviceSerialNo;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "get_user_data", nullable = false)
    private UserFlagStatus getUserData = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "set_user_data", nullable = false)
    private UserFlagStatus setUserData = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "get_next_user_data_ext", nullable = false)
    private UserFlagStatus getNextUserDataExt = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "get_user_password", nullable = false)
    private UserFlagStatus getUserPassword = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "get_user_card_no", nullable = false)
    private UserFlagStatus getUserCardNo = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "enroll_face_by_photo", nullable = false)
    private UserFlagStatus enrollFaceByPhoto = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "get_finger_data", nullable = false)
    private UserFlagStatus getFingerData = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "set_finger_data", nullable = false)
    private UserFlagStatus setFingerData = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "get_face_data", nullable = false)
    private UserFlagStatus getFaceData = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "set_face_data", nullable = false)
    private UserFlagStatus setFaceData = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "get_user_photo", nullable = false)
    private UserFlagStatus getUserPhoto = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "set_user_photo", nullable = false)
    private UserFlagStatus setUserPhoto = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "get_first_glog", nullable = false)
    private UserFlagStatus getFirstGlog = UserFlagStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "get_next_glog", nullable = false)
    private UserFlagStatus getNextGlog = UserFlagStatus.DRAFT;

}
