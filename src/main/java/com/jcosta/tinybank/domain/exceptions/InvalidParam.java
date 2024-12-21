package com.jcosta.tinybank.domain.exceptions;

public class InvalidParam {
    private final String param;
    private final String reason;

    public InvalidParam(String param, String reason) {
        this.param = param;
        this.reason = reason;
    }

    public String getParam() {
        return param;
    }

    public String getReason() {
        return reason;
    }
}
