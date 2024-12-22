package com.jcosta.tinybank.application.usecases.accounts;

public record CreateAccountRequest(String ownerId, long balance) {
}
