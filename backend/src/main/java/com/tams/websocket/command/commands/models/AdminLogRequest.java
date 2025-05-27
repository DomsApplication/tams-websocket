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
public class AdminLogRequest extends BaseRequestMessage {

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

    @JacksonXmlProperty(localName = "TransID")
    private String transID;

    @JacksonXmlProperty(localName = "LogID")
    private String logID;

    @JacksonXmlProperty(localName = "Time")
    private String time;

    @JacksonXmlProperty(localName = "AdminID")
    private String adminID;

    @JacksonXmlProperty(localName = "UserID")
    private String userID;

    @JacksonXmlProperty(localName = "Action")
    private String action;

    @JacksonXmlProperty(localName = "Stat")
    private String stat;
}
