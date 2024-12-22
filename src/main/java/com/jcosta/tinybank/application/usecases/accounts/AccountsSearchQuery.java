package com.jcosta.tinybank.application.usecases.accounts;

public record AccountsSearchQuery(String ownerId, Integer limit, String cursor) {
}
