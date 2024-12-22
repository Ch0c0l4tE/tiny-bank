package com.jcosta.tinybank.domain.exceptions;

public class InvalidParam {
    private final String name;
    private final String reason;

    @Override
    public String toString() {
        return "InvalidParam{" +
                "name='" + name + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }

    public InvalidParam(String name, String reason) {
        this.name = name;
        this.reason = reason;
    }

    public String getName() {
        return name;
    }

    public String getReason() {
        return reason;
    }
}
