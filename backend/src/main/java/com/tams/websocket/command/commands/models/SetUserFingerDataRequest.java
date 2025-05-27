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
public class SetUserFingerDataRequest extends BaseRequestMessage {

    @JacksonXmlProperty(localName = "UserID")
    private String userID;

    @JacksonXmlProperty(localName = "Privilege")
    private String privilege;

    @JacksonXmlProperty(localName = "DuplicationCheck")
    private String duplicationCheck;

    @JacksonXmlProperty(localName = "FingerNo")
    private String fingerNo;

    @JacksonXmlProperty(localName = "Duress")
    private String duress;

    @JacksonXmlProperty(localName = "FingerData")
    private String fingerData;


}
