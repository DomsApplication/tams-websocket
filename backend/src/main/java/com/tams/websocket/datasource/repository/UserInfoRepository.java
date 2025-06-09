package com.tams.websocket.datasource.repository;

import com.tams.websocket.datasource.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInfoRepository  extends JpaRepository<UserInfo, String> {

    Optional<UserInfo> findByUserId(String userId);

    @Query(value = "select ui.user_id, ui.name, ui.privilege, ui.enabled, ui.department_id, uie.face_enrolled as face_data_available, " +
            "(select count(id) from user_fingers as uf where uf.user_id = ui.user_id and uf.enrolled = true and uf.finger_data is not null) as finger_data_available,  " +
            "uie.photo_captured as photo_data_available, uie.password, uie.card " +
            "from user_info as ui, user_info_ext as uie  " +
            "where ui.user_id = uie.user_id ",
            nativeQuery = true)
    List<Object[]> findUserDetails();

    @Query(value = "select ui.user_id, ui.name, ui.privilege, ui.enabled, ui.department_id, uie.face_enrolled as face_data_available, " +
            "(select count(id) from user_fingers as uf where uf.user_id = ui.user_id and uf.enrolled = true and uf.finger_data is not null) as finger_data_available,  " +
            "uie.photo_captured as photo_data_available, uie.password, uie.card " +
            "from user_info as ui, user_info_ext as uie  " +
            "where ui.user_id = :userId and ui.user_id = uie.user_id ",
            nativeQuery = true)
    List<Object[]> findUserDetailsByUserId(@Param("userId") String userId);

    @Query(value = "SELECT (SELECT COUNT(id) FROM user_info WHERE is_deleted = false) AS registered_user_count, " +
            "(SELECT COUNT(id) FROM device_info WHERE is_deleted = false) AS registered_device_count, " +
            "(SELECT COUNT(DISTINCT device_serial_no)  FROM device_log WHERE created_on >= NOW() - INTERVAL 30 MINUTE) AS active_device_count, " +
            "(select count(*) from time_log WHERE created_on >= NOW() - INTERVAL 30 MINUTE) AS last_time_log_count ",
            nativeQuery = true)
    Object[] findDashboardWidgetsData();

}
