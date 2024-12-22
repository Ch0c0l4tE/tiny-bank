package com.jcosta.tinybank.application.usecases.transactions;

import com.jcosta.tinybank.application.port.TransactionsDataService;

public class CreateTransaction {
    private final TransactionsDataService transactionsDataService;

    public CreateTransaction(TransactionsDataService transactionsDataService){
        this.transactionsDataService = transactionsDataService;
    }
}
