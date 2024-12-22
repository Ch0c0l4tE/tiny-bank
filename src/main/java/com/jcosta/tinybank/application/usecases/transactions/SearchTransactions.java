package com.jcosta.tinybank.application.usecases.transactions;

import com.jcosta.tinybank.application.port.TransactionsDataService;

public class SearchTransactions {
    private final TransactionsDataService transactionsDataService;

    public SearchTransactions(TransactionsDataService transactionsDataService){
        this.transactionsDataService = transactionsDataService;
    }
}
