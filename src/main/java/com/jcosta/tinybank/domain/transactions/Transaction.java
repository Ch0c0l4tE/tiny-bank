package com.jcosta.tinybank.domain.transactions;

import com.jcosta.tinybank.domain.exceptions.DomainException;
import com.jcosta.tinybank.domain.exceptions.ExceptionCode;
import com.jcosta.tinybank.domain.exceptions.InvalidParam;

import java.util.ArrayList;
import java.util.List;

public record Transaction(TransactionOperationType operationType, String source, String target, long amount, long epochMillis) {
    public Transaction {
        List<InvalidParam> errors = new ArrayList<>();

        if (!operationType.equals(TransactionOperationType.TRANSFER)) {
            if(operationType.equals(TransactionOperationType.WITHDRAW) && source == null) {
                errors.add(new InvalidParam("source", "Withdraws cant have source account null"));
            } else if (operationType.equals(TransactionOperationType.DEPOSIT) && target == null) {
                errors.add(new InvalidParam("target", "Deposits cant have a target account null"));
            }
        }

        if (amount <= 0) {
            throw new DomainException(
                    ExceptionCode.VALIDATION_EXCEPTION,
                    new InvalidParam("amount", "Cant perform transactions with negative amounts"));
        }

        if(errors.size() > 0) {
            throw new DomainException(
                    ExceptionCode.VALIDATION_EXCEPTION,
                    errors);
        }
    }
}
