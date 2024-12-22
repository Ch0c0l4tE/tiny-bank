package com.jcosta.tinybank.domain.users;

import com.jcosta.tinybank.domain.exceptions.BusinessException;
import com.jcosta.tinybank.domain.exceptions.DomainException;
import com.jcosta.tinybank.domain.exceptions.ExceptionCode;
import com.jcosta.tinybank.domain.exceptions.InvalidParam;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public record User(String id, String username) {
    public User(String id, String username) {
        this.id = id;
        this.username = username;
    }
}
