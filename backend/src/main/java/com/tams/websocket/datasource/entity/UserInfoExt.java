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
@Table(name = "user_info_ext")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
public class UserInfoExt extends BaseEntity {

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "card")
    private String card;

    @Column(name = "password")
    private String password;

    @Column(name = "face_enrolled", nullable = false)
    private Boolean faceEnrolled = false;

    @Lob
    @Column(name = "face_data", columnDefinition = "MEDIUMBLOB")
    private byte[] faceData;

    @Column(name = "photo_captured", nullable = false)
    private Boolean photoCaptured = false;

    @Column(name = "photo_size")
    private Integer photoSize;

    @Lob
    @Column(name = "photo_data", columnDefinition = "MEDIUMBLOB")
    private byte[] photoData;

    @Column(name = "time_set_1")
    private String timeSet1;

    @Column(name = "time_set_2")
    private String timeSet2;

    @Column(name = "time_set_3")
    private String timeSet3;

    @Column(name = "time_set_4")
    private String timeSet4;

    @Column(name = "time_set_5")
    private String timeSet5;
}
