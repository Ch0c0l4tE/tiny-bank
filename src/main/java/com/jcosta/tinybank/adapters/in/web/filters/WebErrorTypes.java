package com.jcosta.tinybank.adapters.in.web.filters;

import com.jcosta.tinybank.domain.exceptions.ExceptionCode;

public class WebErrorTypes {
    public static String getWebErrorType(ExceptionCode code) {
        switch (code){
            case VALIDATION_EXCEPTION -> {
                return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.1";
            }
            case DUPLICATE_EXCEPTION -> {
                return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.8";
            }
            case NOT_FOUND_EXCEPTION -> {
                return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.4";
            }
            case OPERATION_NOT_ALLOWED -> {
                return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.5";
            }
            default -> {
                return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.6.1";
            }
        }
    }

    public static int getWebErrorStatus(ExceptionCode code) {
        switch (code){
            case VALIDATION_EXCEPTION -> {
                return 400;
            }
            case DUPLICATE_EXCEPTION -> {
                return 409;
            }
            case NOT_FOUND_EXCEPTION -> {
                return 404;
            }
            case OPERATION_NOT_ALLOWED -> {
                return 405;
            }
            default -> {
                return 500;
            }
        }
    }
}
