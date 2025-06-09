package com.tams.websocket.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LicenseValidationAspect {

    //@Autowired
    //private LicenseService licenseService; // Your existing LicenseService

    @PersistenceContext
    private EntityManager entityManager; // For dynamic entity handling

    @Before("execution(* org.springframework.data.jpa.repository.JpaRepository.save(..))")
    public void validateLicenseBeforeSave(JoinPoint joinPoint) {
        Object entity = joinPoint.getArgs()[0]; // The entity being saved

        if (entity != null) {
            Class<?> entityClass = entity.getClass();
            String entityName = entityClass.getSimpleName();

            // Perform a dynamic count query for the specific entity
            long entityCount = getEntityCount(entityClass);

            log.info("######## The entity '" + entityName + "' have '" + entityCount + "' of records........");
            // Fetch license restrictions for the current entity
            //int maxAllowed = licenseService.getMaxLimitForEntity(entityName);

            //if (entityCount >= maxAllowed) {
            //throw new IllegalStateException("License limit exceeded for " + entityName + ". Cannot create more records.");
            //}
        }
    }

    private long getEntityCount(Class<?> entityClass) {
        String entityName = entityClass.getSimpleName();
        String query = "SELECT COUNT(e) FROM " + entityName + " e";
        return entityManager.createQuery(query, Long.class).getSingleResult();
    }
}
