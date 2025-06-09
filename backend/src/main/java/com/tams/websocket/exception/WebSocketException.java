package com.tams.websocket.exception;

public class WebSocketException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public WebSocketException() {
        this("");
    }

    public WebSocketException(String message) {
        super(message);
    }

    public WebSocketException(Throwable cause) {
        super(cause);
    }

    public WebSocketException(String message, Throwable cause) {
        super(message, cause);
    }

}