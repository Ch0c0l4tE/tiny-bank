package com.jcosta.tinybank.adapters.out.storage.inmemory;

import com.jcosta.tinybank.application.port.TransactionsDataService;
import com.jcosta.tinybank.domain.transactions.Transaction;

public class TransactionsStorage implements TransactionsDataService {
    @Override
    public Transaction create(Transaction transaction) {
        return null;
    }

    @Override
    public Transaction search(String accountId) {
        return null;
    }
}
