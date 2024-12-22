package com.jcosta.tinybank.application.usecases.transactions;

public record SearchTransactionsQuery (String accountId, Integer limit, String cursor) {
}
