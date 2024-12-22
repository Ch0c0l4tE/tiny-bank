package com.jcosta.tinybank.application.port;

import com.jcosta.tinybank.domain.Search;
import com.jcosta.tinybank.domain.accounts.Account;
import com.jcosta.tinybank.domain.users.User;

public interface AccountsDataService {
    Account create(Account account);

    Account get(String id, boolean includeInactive);

    Search<Account> search(String ownerId, Integer limit, String cursor, boolean includeInactive);

    boolean update(Account account);
}
