package com.jcosta.tinybank.application.usecases.transactions;

import com.jcosta.tinybank.application.port.TransactionsDataService;
import com.jcosta.tinybank.application.port.AccountsDataService;
import com.jcosta.tinybank.domain.accounts.Account;
import com.jcosta.tinybank.domain.exceptions.DomainException;
import com.jcosta.tinybank.domain.exceptions.InvalidParam;
import com.jcosta.tinybank.domain.transactions.Transaction;
import com.jcosta.tinybank.domain.transactions.TransactionOperationType;
import com.jcosta.tinybank.domain.exceptions.BusinessException;
import com.jcosta.tinybank.domain.exceptions.ExceptionCode;
import com.jcosta.tinybank.domain.users.UserStatus;

import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CreateTransaction {
    private final TransactionsDataService transactionsDataService;
    private final AccountsDataService accountsDataService;

    public CreateTransaction(
            TransactionsDataService transactionsDataService,
            AccountsDataService accountsDataService){
        this.transactionsDataService = transactionsDataService;
        this.accountsDataService = accountsDataService;
    }

    public Transaction execute(CreateTransactionRequest createTransactionRequest) {
        TransactionOperationType operationType;

        try {
            operationType = TransactionOperationType.valueOf(createTransactionRequest.operationType());
        } catch (Exception ex) {
            throw new DomainException(
                    ExceptionCode.VALIDATION_EXCEPTION,
                    new InvalidParam(
                            "operationType",
                            String.format(
                                    "Invalid operationType, must be one of: %s",
                                    String.join(",", Arrays.stream(TransactionOperationType.values()).map(Enum::toString).collect(Collectors.joining(","))))));
        }

        Transaction transaction = new Transaction(
                operationType,
                createTransactionRequest.source(),
                createTransactionRequest.target(),
                createTransactionRequest.amount(),
                Instant.now().toEpochMilli());

        if(transaction.operationType().equals(TransactionOperationType.TRANSFER)) {
            Account sourceAccount = this.accountsDataService.get(transaction.source(), false);

            if (sourceAccount == null) {
                throw new BusinessException(
                        ExceptionCode.NOT_FOUND_EXCEPTION,
                        String.format("source account with id [%s] not fount", transaction.source()));
            }

            Account targetAccount = this.accountsDataService.get(transaction.target(), false);

            if (targetAccount == null) {
                throw new BusinessException(
                        ExceptionCode.NOT_FOUND_EXCEPTION,
                        String.format("target account with id [%s] not fount", transaction.target()));
            }

            if (sourceAccount.balance() - transaction.amount() < 0) {
                throw new BusinessException(
                        ExceptionCode.NOT_FOUND_EXCEPTION,
                        "source account does not have enough founds");
            }

            this.accountsDataService.update(
                    new Account(
                            sourceAccount.id(),
                            sourceAccount.ownerId(),
                            sourceAccount.balance() - transaction.amount(),
                            sourceAccount.status()));

            this.accountsDataService.update(
                    new Account(
                            targetAccount.id(),
                            targetAccount.ownerId(),
                            targetAccount.balance() + transaction.amount(),
                            targetAccount.status()));
        } else if (transaction.operationType().equals(TransactionOperationType.WITHDRAW)) {
            Account sourceAccount = this.accountsDataService.get(transaction.source(), false);

            if (sourceAccount == null) {
                throw new BusinessException(
                        ExceptionCode.NOT_FOUND_EXCEPTION,
                        String.format("source account with id [%s] not fount", transaction.target()));
            }

            if (sourceAccount.balance() - transaction.amount() < 0) {
                throw new BusinessException(
                        ExceptionCode.NOT_FOUND_EXCEPTION,
                        "source account does not have enough founds");
            }

            this.accountsDataService.update(
                    new Account(
                            sourceAccount.id(),
                            sourceAccount.ownerId(),
                            sourceAccount.balance() - transaction.amount(),
                            sourceAccount.status()));
        } else if (transaction.operationType().equals(TransactionOperationType.DEPOSIT)) {
            Account targetAccount = this.accountsDataService.get(transaction.target(), false);

            if (targetAccount == null) {
                throw new BusinessException(
                        ExceptionCode.NOT_FOUND_EXCEPTION,
                        String.format("target account with id [%s] not fount", transaction.target()));
            }

            this.accountsDataService.update(
                    new Account(
                            targetAccount.id(),
                            targetAccount.ownerId(),
                            targetAccount.balance() + transaction.amount(),
                            targetAccount.status()));
        }

        return this.transactionsDataService.create(transaction);
    }
}
