package com.tams.websocket.audit;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class TraceInfo {

    private String traceId;
    private String sessionId;
    private String command;
    private String deviceSerialNo;
    private String hostAddress;
    private String hostPort;
    private String userId;
    private String deptId;

}
