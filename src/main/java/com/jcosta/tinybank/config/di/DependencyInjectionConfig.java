package com.jcosta.tinybank.config.di;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcosta.tinybank.adapters.out.storage.inmemory.AccountsStorage;
import com.jcosta.tinybank.adapters.out.storage.inmemory.TransactionsStorage;
import com.jcosta.tinybank.adapters.out.storage.inmemory.UsersStorage;
import com.jcosta.tinybank.application.port.AccountsDataService;
import com.jcosta.tinybank.application.port.TransactionsDataService;
import com.jcosta.tinybank.application.port.UserDataService;
import com.jcosta.tinybank.application.usecases.accounts.CreateAccount;
import com.jcosta.tinybank.application.usecases.accounts.GetAccountById;
import com.jcosta.tinybank.application.usecases.accounts.SearchAccounts;
import com.jcosta.tinybank.application.usecases.transactions.CreateTransaction;
import com.jcosta.tinybank.application.usecases.transactions.SearchTransactions;
import com.jcosta.tinybank.application.usecases.users.CreateUser;
import com.jcosta.tinybank.application.usecases.users.GetUserById;
import com.jcosta.tinybank.application.usecases.users.PatchUser;
import com.jcosta.tinybank.application.usecases.users.SearchUsers;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Produces;

public class DependencyInjectionConfig {


    // Adapters
    @Produces
    @ApplicationScoped
    UserDataService produceUserDataService() {
        return new UsersStorage();
    }

    @Produces
    @ApplicationScoped
    AccountsDataService produceAccountsDataService() {
        return new AccountsStorage();
    }

    @Produces
    @ApplicationScoped
    TransactionsDataService produceTransactionsDataService() {
        return new TransactionsStorage();
    }

    // Application
    @Produces
    @ApplicationScoped
    CreateUser produceCreateUser(UserDataService userDataService) {
        return new CreateUser(userDataService);
    }

    @Produces
    @ApplicationScoped
    GetUserById produceGetUser(UserDataService userDataService) {
        return new GetUserById(userDataService);
    }

    @Produces
    @ApplicationScoped
    PatchUser producePatchUser(UserDataService userDataService, AccountsDataService accountsDataService, ObjectMapper objectMapper) {
        return new PatchUser(userDataService, accountsDataService, objectMapper);
    }

    @Produces
    @ApplicationScoped
    SearchUsers produceSearchUsers(UserDataService userDataService) {
        return new SearchUsers(userDataService);
    }

    @Produces
    @ApplicationScoped
    CreateAccount produceCreateAccount(AccountsDataService accountsDataService, UserDataService userDataService) {
        return new CreateAccount(accountsDataService, userDataService);
    }

    @Produces
    @ApplicationScoped
    GetAccountById produceGetAccountById(AccountsDataService accountsDataService, UserDataService userDataService) {
        return new GetAccountById(accountsDataService, userDataService);
    }

    @Produces
    @ApplicationScoped
    SearchAccounts produceSearchAccounts(AccountsDataService accountsDataService) {
        return new SearchAccounts(accountsDataService);
    }

    @Produces
    @ApplicationScoped
    CreateTransaction produceCreateTransaction(
            TransactionsDataService transactionsDataService,
            AccountsDataService accountsDataService) {
        return new CreateTransaction(transactionsDataService, accountsDataService);
    }

    @Produces
    @ApplicationScoped
    SearchTransactions produceSearchTransactions(TransactionsDataService transactionsDataService) {
        return new SearchTransactions(transactionsDataService);
    }
}
