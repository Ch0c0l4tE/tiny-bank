package com.jcosta.tinybank.application.usecases.accounts;

import com.jcosta.tinybank.application.port.AccountsDataService;
import com.jcosta.tinybank.domain.Search;
import com.jcosta.tinybank.domain.accounts.Account;

public class SearchAccounts {

    private final AccountsDataService accountsDataService;

    public SearchAccounts(AccountsDataService accountsDataService){

        this.accountsDataService = accountsDataService;
    }

    public Search<Account> execute(AccountsSearchQuery accountsSearchQuery) {
        return this.accountsDataService.search(
                accountsSearchQuery.ownerId(),
                accountsSearchQuery.limit(),
                accountsSearchQuery.cursor(),
                false);
    }
}
