package com.tams.websocket.datasource.repository;

import com.tams.websocket.datasource.entity.UserCommands;
import com.tams.websocket.datasource.entity.enums.UserFlagStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor // Lombok's annotation to generate a constructor with required args
@Repository
public class UserCommandsRepositoryCustom {

    @Autowired
    private final EntityManager entityManager;

    public List<Map<String, Object>> findUserCommandFlagWithNewStatus(String deviceSerialNo, int limit) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<UserCommands> root = query.from(UserCommands.class);

        // Define the selection dynamically for columns that are 'NEW'
        Expression<String> deviceSerialNoExpr = root.get("deviceSerialNo");
        Expression<String> userIdExpr = root.get("userId");

        // Initialize selections with basic fields
        List<Selection<?>> selections = new ArrayList<>();
        selections.add(deviceSerialNoExpr);
        selections.add(userIdExpr);

        // Keep track of column names in the same order as selections
        List<String> columnNames = new ArrayList<>();
        columnNames.add("device_serial_no");
        columnNames.add("user_id");

        // Add fields dynamically where status is 'NEW'
        for (Field field : UserCommands.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class) && field.getType().equals(UserFlagStatus.class)) {
                String fieldName = field.getName();
                selections.add(root.get(fieldName)); // Add to selections
                columnNames.add(fieldName); // Add corresponding column name
            }
        }

        // Build query
        query.multiselect(selections.toArray(new Selection[0])); // Correct array type
        query.where(cb.equal(deviceSerialNoExpr, deviceSerialNo));
        query.orderBy(cb.desc(root.get("createdOn"))); // Assuming `createdOn` is a valid field

        // Limit results
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
        typedQuery.setMaxResults(limit);

        // Fetch results and map to a List of Maps
        List<Object[]> results = typedQuery.getResultList();
        List<Map<String, Object>> mappedResults = new ArrayList<>();

        // Iterate over the result and map to column names
        for (Object[] row : results) {
            Map<String, Object> rowMap = new LinkedHashMap<>();
            for (int i = 0; i < row.length; i++) {
                if (columnNames.get(i).equals("device_serial_no") || columnNames.get(i).equals("user_id") || row[i].toString().equals("NEW")) {
                    rowMap.put(columnNames.get(i), row[i]); // Map column name to value
                }
            }
            mappedResults.add(rowMap);
        }
        return mappedResults;
    }

    @Transactional
    public void updateSingleUserData(String columnName, String columnValue, String deviceSerialNo, String userId) {
        try {
            // CriteriaBuilder to build the update query
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaUpdate<UserCommands> update = cb.createCriteriaUpdate(UserCommands.class);
            Root<UserCommands> root = update.from(UserCommands.class);

            // Dynamically set the column value based on the input
            update.set(root.get(columnName), columnValue);

            // Add the condition for deviceSerialNo and userId
            update.where(cb.equal(root.get("deviceSerialNo"), deviceSerialNo));
            update.where(cb.equal(root.get("userId"), userId));

            // Execute the update
            entityManager.createQuery(update).executeUpdate();
        } catch (InvalidDataAccessApiUsageException aue) {
            log.error("The command: '"+columnName+"' not fund in the 'user_commands' Table ", aue.getMessage());
        } catch (Exception e) {
            log.error("The command: '"+columnName+"' not fund in the 'user_commands' Table ", e.getMessage());
        }
    }

}
