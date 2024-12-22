package com.jcosta.tinybank.domain.users;

import com.jcosta.tinybank.domain.exceptions.DomainException;
import com.jcosta.tinybank.domain.exceptions.ExceptionCode;
import com.jcosta.tinybank.domain.exceptions.InvalidParam;

import java.util.ArrayList;
import java.util.List;

public record User(String id, String username, UserStatus status) {
    public User{
        List<InvalidParam> invalidParams = new ArrayList<>();

        if (username == null || username.isEmpty()) {
            invalidParams.add(new InvalidParam("ownerId", "username can't be null or empty"));
        }

        if(invalidParams.size()>0) {
            throw new DomainException(ExceptionCode.VALIDATION_EXCEPTION, invalidParams);
        }
    }
}
