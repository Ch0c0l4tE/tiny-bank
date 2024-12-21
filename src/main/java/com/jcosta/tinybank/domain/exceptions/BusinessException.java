package com.jcosta.tinybank.domain.exceptions;

public class BusinessException extends RuntimeException {
    public BusinessException(ExceptionCode code, String message, String developerMessage, Exception ex) {

    }
}
