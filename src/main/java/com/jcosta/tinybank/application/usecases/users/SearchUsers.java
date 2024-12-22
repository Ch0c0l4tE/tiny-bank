package com.jcosta.tinybank.application.usecases.users;

import com.jcosta.tinybank.application.port.UserDataService;
import com.jcosta.tinybank.domain.Search;
import com.jcosta.tinybank.domain.users.User;

public class SearchUsers {
    private final UserDataService userDataService;

    public SearchUsers(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    public Search<User> execute(UserSearchQuery userSearchQuery) {
        return this.userDataService.search(
                userSearchQuery.username(),
                userSearchQuery.limit(),
                userSearchQuery.cursor(),
                false);
    }
}
