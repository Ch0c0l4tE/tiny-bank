package com.jcosta.tinybank.application.usecases.users;

import com.jcosta.tinybank.application.port.UserDataService;
import com.jcosta.tinybank.domain.users.User;
import org.jboss.logging.Logger;

import static io.quarkiverse.loggingjson.providers.KeyValueStructuredArgument.kv;

public class CreateUser {
    private static final Logger LOG = Logger.getLogger(CreateUser.class);
    private final UserDataService userDataService;

    public CreateUser(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    public User execute(CreateUserRequest createUserRequest) {
        return this.userDataService.create(createUserRequest.toModel());
    }
}
