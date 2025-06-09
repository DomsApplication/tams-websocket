package com.tams.websocket.command.commands;

import com.tams.websocket.audit.TraceInfo;
import com.tams.websocket.socket.SessionInfo;

public interface Command {

    void init(Command command);

    String sender(TraceInfo traceInfo);

    void execute(SessionInfo sessionInfo, TraceInfo traceInfo);

}
