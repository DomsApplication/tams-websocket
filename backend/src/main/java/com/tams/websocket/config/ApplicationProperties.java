package com.tams.websocket.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class ApplicationProperties {

    private int tokenExpiryMinutes;

    private List<String> allowedOrigins;
}

