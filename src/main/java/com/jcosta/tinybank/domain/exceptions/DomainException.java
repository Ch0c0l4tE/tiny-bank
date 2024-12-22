package com.jcosta.tinybank.domain.exceptions;

import java.util.List;

public class DomainException extends RuntimeException {
    private final ExceptionCode code;
    private final List<InvalidParam> invalidParams;

    public DomainException(ExceptionCode code, List<InvalidParam> invalidParams, Throwable cause) {
        super(cause);
        this.code = code;
        this.invalidParams = invalidParams;
    }

    public DomainException(ExceptionCode code, List<InvalidParam> invalidParamList) {
        this(code, invalidParamList, null);
    }

    public List<InvalidParam> getInvalidParams() {
        return invalidParams;
    }

    public ExceptionCode getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "DomainException{" +
                "code=" + code +
                ", invalidParams=" + invalidParams +
                '}';
    }
}
