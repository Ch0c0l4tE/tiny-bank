package com.jcosta.tinybank.application.usecases.users;

import com.jcosta.tinybank.application.port.UserDataService;

public class SearchUsers {
    private final UserDataService userDataService;

    public SearchUsers(UserDataService userDataService) {
        this.userDataService = userDataService;
    }
}
