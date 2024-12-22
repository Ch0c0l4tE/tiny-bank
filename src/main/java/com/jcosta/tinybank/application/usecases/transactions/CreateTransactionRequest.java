package com.jcosta.tinybank.application.usecases.transactions;

import com.jcosta.tinybank.domain.transactions.TransactionOperationType;

public record CreateTransactionRequest(String operationType, String source, String target, Long amount) {
}
