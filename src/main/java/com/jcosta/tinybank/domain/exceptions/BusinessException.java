package com.jcosta.tinybank.domain.exceptions;

import java.util.List;

public class BusinessException extends RuntimeException {
    private final ExceptionCode code;
    private final String message;

    public BusinessException(ExceptionCode code, String message, Throwable cause) {
        super(cause);
        this.code = code;
        this.message = message;
    }

    public BusinessException(ExceptionCode code, String message) {
        this(code, message, null);
    }

    public ExceptionCode getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
