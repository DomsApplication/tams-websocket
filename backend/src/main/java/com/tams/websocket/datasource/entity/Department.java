package com.tams.websocket.datasource.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

@Entity
@Table(name = "department")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
public class Department extends BaseEntity {

    @Column(name = "department_id")
    private String departmentId;

    @Column(name = "department_name")
    private String departmentName;

}
