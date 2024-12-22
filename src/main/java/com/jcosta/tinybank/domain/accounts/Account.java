package com.jcosta.tinybank.domain.accounts;

import com.jcosta.tinybank.domain.exceptions.DomainException;
import com.jcosta.tinybank.domain.exceptions.ExceptionCode;
import com.jcosta.tinybank.domain.exceptions.InvalidParam;

public record Account(String id, String ownerId, long balance, AccountStatus status) {
    public Account {
        if (balance < 0) {
            throw new DomainException(ExceptionCode.VALIDATION_EXCEPTION, new InvalidParam("balance", "Balance can't be negative"));
        }
    }
}
