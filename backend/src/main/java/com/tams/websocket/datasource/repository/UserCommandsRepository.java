package com.tams.websocket.datasource.repository;

import com.tams.websocket.datasource.entity.UserCommands;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCommandsRepository extends JpaRepository<UserCommands, String> {

    @Query("SELECT uc FROM UserCommands uc WHERE uc.userId= :userId ")
    List<UserCommands> findrecordsByuserId(@Param("userId") String userId);

    @Query("SELECT uc FROM UserCommands uc WHERE uc.deviceSerialNo = :deviceSerialNo ")
    List<UserCommands> findrecordsByDeviceSerialNo(@Param("deviceSerialNo") String deviceSerialNo);

    @Query("SELECT uc FROM UserCommands uc WHERE uc.deviceSerialNo = :deviceSerialNo and uc.userId= :userId ")
    Optional<UserCommands> findUserCommandByDeviceSerialNoAndUserId(@Param("deviceSerialNo") String deviceSerialNo, @Param("userId") String userId);

}
