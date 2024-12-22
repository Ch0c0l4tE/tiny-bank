package com.jcosta.tinybank.adapters.in.web.transactions;

import com.jcosta.tinybank.domain.transactions.TransactionOperationType;

public record TransactionDto(TransactionOperationType operationType, String source, String target, long amount, String occurredAt){}