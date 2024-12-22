package com.jcosta.tinybank.application.port;

import com.jcosta.tinybank.domain.transactions.Transaction;

public interface TransactionsDataService {
    Transaction create(Transaction transaction);

    Transaction search(String accountId);
}
