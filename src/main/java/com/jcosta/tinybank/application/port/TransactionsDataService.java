package com.jcosta.tinybank.application.port;

import com.jcosta.tinybank.domain.Search;
import com.jcosta.tinybank.domain.transactions.Transaction;

public interface TransactionsDataService {
    Transaction create(Transaction transaction);

    Search<Transaction> search(String accountId, Integer limit, String cursor);
}
