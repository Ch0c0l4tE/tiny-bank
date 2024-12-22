package com.jcosta.tinybank.application.usecases.accounts;

import com.jcosta.tinybank.application.port.AccountsDataService;
import com.jcosta.tinybank.application.port.UserDataService;
import com.jcosta.tinybank.domain.Search;
import com.jcosta.tinybank.domain.accounts.Account;
import com.jcosta.tinybank.domain.accounts.AccountStatus;
import com.jcosta.tinybank.domain.exceptions.BusinessException;
import com.jcosta.tinybank.domain.exceptions.ExceptionCode;
import com.jcosta.tinybank.domain.users.User;

public class CreateAccount {

    private final AccountsDataService accountsDataService;
    private final UserDataService userDataService;

    public CreateAccount(
            AccountsDataService accountsDataService,
            UserDataService userDataService) {
        this.accountsDataService = accountsDataService;
        this.userDataService = userDataService;
    }

    public Account execute(CreateAccountRequest createAccountRequest){

        User existingOwner = this.userDataService.get(createAccountRequest.ownerId(), false);

        if(existingOwner == null) {
            throw new BusinessException(
                    ExceptionCode.NOT_FOUND_EXCEPTION,
                    String.format("user with id [%s] not fount", createAccountRequest.ownerId()));
        }

        return this.accountsDataService.create(
                new Account(
                        null,
                        createAccountRequest.ownerId(),
                        createAccountRequest.balance(),
                        AccountStatus.ACTIVE));
    }
}
