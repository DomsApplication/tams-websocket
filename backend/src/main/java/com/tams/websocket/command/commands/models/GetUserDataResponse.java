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
public class GetUserDataResponse extends BaseResponseMessage {

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

    @JacksonXmlProperty(localName = "UserID")
    private String userID;

    @JacksonXmlProperty(localName = "Name")
    private String name;

    @JacksonXmlProperty(localName = "Privilege")
    private String privilege;

    @JacksonXmlProperty(localName = "Depart")
    private String depart;

    @JacksonXmlProperty(localName = "Enabled")
    private String enabled;

    @JacksonXmlProperty(localName = "TimeSet1")
    private String timeSet1;

    @JacksonXmlProperty(localName = "TimeSet2")
    private String timeSet2;

    @JacksonXmlProperty(localName = "TimeSet3")
    private String timeSet3;

    @JacksonXmlProperty(localName = "TimeSet4")
    private String timeSet4;

    @JacksonXmlProperty(localName = "TimeSet5")
    private String timeSet5;

    @JacksonXmlProperty(localName = "UserPeriod_Used")
    private String userPeriodUsed;

    @JacksonXmlProperty(localName = "UserPeriod_Start")
    private String userPeriodStart;

    @JacksonXmlProperty(localName = "UserPeriod_End")
    private String userPeriodEnd;

    @JacksonXmlProperty(localName = "Card")
    private String card;

    @JacksonXmlProperty(localName = "PWD")
    private String pwd;

    @JacksonXmlProperty(localName = "Fingers")
    private String fingers;

    @JacksonXmlProperty(localName = "FaceEnrolled")
    private String faceEnrolled;

    @JacksonXmlProperty(localName = "FaceData")
    private String faceData;

    @JacksonXmlProperty(localName = "More")
    private String more;

}
