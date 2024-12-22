package com.jcosta.tinybank.application.usecases.users;

public record UserSearchQuery(String username, Integer limit, String cursor) {
}
