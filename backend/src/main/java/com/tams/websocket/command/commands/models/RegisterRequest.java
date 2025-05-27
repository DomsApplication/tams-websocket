package com.tams.websocket.command.commands.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class RegisterRequest extends BaseRequestMessage {

    @JacksonXmlProperty(localName = "TerminalType")
    private String terminalType;

    @JacksonXmlProperty(localName = "ProductName")
    private String productName;

    @JacksonXmlProperty(localName = "DeviceSerialNo")
    private String deviceSerialNo;

    @JacksonXmlProperty(localName = "CloudId")
    private String cloudId;

}
