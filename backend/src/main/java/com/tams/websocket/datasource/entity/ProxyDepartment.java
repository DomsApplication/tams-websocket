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
@Table(name = "proxy_department")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
public class ProxyDepartment extends BaseEntity {

    @Column(name = "proxy_id")
    private String proxyId;

    @Column(name = "proxy_name")
    private String proxyName;

}
