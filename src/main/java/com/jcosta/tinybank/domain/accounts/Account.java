package com.jcosta.tinybank.domain.accounts;

import com.jcosta.tinybank.domain.exceptions.DomainException;
import com.jcosta.tinybank.domain.exceptions.ExceptionCode;
import com.jcosta.tinybank.domain.exceptions.InvalidParam;

import java.util.ArrayList;
import java.util.List;

public record Account(String id, String ownerId, long balance, AccountStatus status) {
    public Account {
        List<InvalidParam> invalidParams = new ArrayList<>();

        if (balance < 0) {
            invalidParams.add(new InvalidParam("balance", "Balance can't be negative"));
        }

        if (ownerId == null || ownerId.isEmpty()) {
            invalidParams.add(new InvalidParam("ownerId", "ownerId can't be null or empty"));
        }

        if(invalidParams.size()>0) {
            throw new DomainException(ExceptionCode.VALIDATION_EXCEPTION, invalidParams);
        }
    }
}
