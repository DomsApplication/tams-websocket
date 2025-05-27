package com.tams.websocket.datasource;

import com.tams.websocket.utils.AESCrypto;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DatabaseConfig {

    @Value("${websocket.database.driver-class-name}")
    private String driverClassName;
    @Value("${websocket.database.host}")
    private String databaseHost;
    @Value("${websocket.database.port}")
    private String databasePort;
    @Value("${websocket.database.name}")
    private String databaseName;
    @Value("${websocket.database.username}")
    private String username;
    @Value("${websocket.database.password}")
    private String password;

    @NonNull
    AESCrypto aesCrypto;

    @Bean
    public DataSource dataSource() {
        String jdbcUrl = String.format("jdbc:mysql://%s:%s/%s", databaseHost, databasePort, databaseName);
        log.info("DataSource:" + jdbcUrl);
        HikariConfig config = new HikariConfig();
        //config.setJdbcUrl(aesCrypto.decrypt(database_url));
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driverClassName);

        // Configure connection pool settings
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(20);
        config.setConnectionTimeout(10000); // Connection timeout in milliseconds
        config.setIdleTimeout(600000); // Idle timeout in milliseconds
        config.setMaxLifetime(1800000); // Maximum lifetime of a connection in milliseconds

        return new HikariDataSource(config);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.shavika.websocket.datasource"); // Adjust package as needed

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update"); // Adjust according to your needs
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect"); // Adjust dialect if necessary

        em.setJpaPropertyMap(properties);
        return em;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
