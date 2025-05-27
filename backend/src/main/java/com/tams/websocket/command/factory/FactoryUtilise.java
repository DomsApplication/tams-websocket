package com.tams.websocket.command.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
@AllArgsConstructor
public class FactoryUtilise {

    private ObjectMapper objectMapper;

    private XmlMapper xmlMapper;

}
