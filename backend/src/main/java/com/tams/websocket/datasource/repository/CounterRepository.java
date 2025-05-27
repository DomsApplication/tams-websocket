package com.tams.websocket.datasource.repository;

import com.tams.websocket.datasource.entity.Counter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterRepository extends JpaRepository<Counter, String> {}

