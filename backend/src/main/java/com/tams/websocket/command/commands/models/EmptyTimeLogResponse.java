package com.tams.websocket.command.commands.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class EmptyTimeLogResponse extends BaseResponseMessage {

    @JacksonXmlProperty(localName = "TerminalType")
    private String terminalType;

    @JacksonXmlProperty(localName = "TerminalID")
    private String terminalID;

    @JacksonXmlProperty(localName = "ProductName")
    private String productName;

    @JacksonXmlProperty(localName = "DeviceSerialNo")
    private String deviceSerialNo;

    @JacksonXmlProperty(localName = "DeviceUID")
    private String deviceUID;

}
