package com.tams.websocket.command.commands.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@JacksonXmlRootElement(localName = "Message")
@Data
@NoArgsConstructor
@SuperBuilder
public abstract class BaseResponseMessage {

    @JacksonXmlProperty(localName = "Response")
    private String response;

    @JacksonXmlProperty(localName = "Result")
    private String result;

    @JacksonXmlProperty(localName = "Reason")
    private String reason;

}