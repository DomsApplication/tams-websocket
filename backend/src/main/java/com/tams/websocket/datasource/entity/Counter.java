package com.tams.websocket.datasource.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "counter")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Counter {

    @Id
    private String name;

    private Long value;
}
