package com.tams.websocket.command.factory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class DependencyFactory {

    @Bean
    @Primary
    public ObjectMapper objectMapperInstance() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Bean
    public XmlMapper XmlMapperInstance() {
        return new XmlMapper();
    }

    @Bean
    public FactoryUtilise factoryUtilise(ObjectMapper objectMapperInstance, XmlMapper XmlMapperInstance) {
        return new FactoryUtilise(objectMapperInstance, XmlMapperInstance);
    }
}
