package com.jcosta.tinybank.application.usecases.accounts;

import com.jcosta.tinybank.application.port.AccountsDataService;
import com.jcosta.tinybank.application.port.UserDataService;
import com.jcosta.tinybank.application.usecases.users.GetUserById;
import com.jcosta.tinybank.domain.accounts.Account;
import com.jcosta.tinybank.domain.exceptions.BusinessException;
import com.jcosta.tinybank.domain.exceptions.ExceptionCode;
import com.jcosta.tinybank.domain.users.User;

public class GetAccountById {
    private final AccountsDataService accountsDataService;
    private final UserDataService userDataService;

    public GetAccountById(AccountsDataService accountsDataService, UserDataService userDataService){
        this.accountsDataService = accountsDataService;
        this.userDataService = userDataService;
    }

    public Account execute(String id) {
        Account account = this.accountsDataService.get(id, true);

        if (account == null) {
            throw new BusinessException(
                    ExceptionCode.NOT_FOUND_EXCEPTION,
                    String.format("account with id [%s] not fount", id));
        }

        User owner = this.userDataService.get(account.ownerId(), false);

        if (owner == null) {
            throw new BusinessException(
                    ExceptionCode.NOT_FOUND_EXCEPTION,
                    String.format("owner with id [%s] not fount", id));
        }

        return account;
    }
}
