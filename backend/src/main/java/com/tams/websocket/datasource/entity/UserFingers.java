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
@Table(name = "user_fingers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
public class UserFingers extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "finger_id")
    private Integer fingerId;

    @Column(name = "enrolled", nullable = false)
    private Boolean enrolled = false;

    @Column(name = "duress", nullable = false)
    private Boolean duress = false;

    @Lob
    @Column(name = "finger_data", columnDefinition = "MEDIUMBLOB")
    private byte[] fingerData;
}
