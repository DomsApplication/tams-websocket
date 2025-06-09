package com.tams.websocket.datasource.repository;

import com.tams.websocket.datasource.entity.UserFingers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFingersRepository  extends JpaRepository<UserFingers, String> {

    List<UserFingers> findByUserId(String userId);

    Optional<UserFingers> findByUserIdAndFingerId(String userId, Integer fingerId);

    @Query("SELECT uf FROM UserFingers uf WHERE uf.userId = :userId AND uf.enrolled = true AND uf.fingerData IS NOT NULL")
    List<UserFingers> findEnrolledUserFingersWithFingerData(@Param("userId") String userId);
}

