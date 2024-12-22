package com.jcosta.tinybank.application.usecases.transactions;

import com.jcosta.tinybank.application.port.TransactionsDataService;
import com.jcosta.tinybank.application.usecases.users.UserSearchQuery;
import com.jcosta.tinybank.domain.Search;
import com.jcosta.tinybank.domain.transactions.Transaction;

public class SearchTransactions {
    private final TransactionsDataService transactionsDataService;

    public SearchTransactions(TransactionsDataService transactionsDataService){
        this.transactionsDataService = transactionsDataService;
    }

    public Search<Transaction> execute(SearchTransactionsQuery query) {
        return this.transactionsDataService.search(
                query.accountId(),
                query.limit(),
                query.cursor());
    }
}
