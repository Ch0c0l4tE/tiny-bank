package com.jcosta.tinybank.application.usecases.users;

import com.jcosta.tinybank.domain.users.User;

public record CreateUserRequest(String username) {

    public User
    toModel() {
        return new User(null, username);
    }
}
