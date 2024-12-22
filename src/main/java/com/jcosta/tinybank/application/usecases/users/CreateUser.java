package com.jcosta.tinybank.application.usecases.users;

import com.jcosta.tinybank.application.port.UserDataService;
import com.jcosta.tinybank.domain.Search;
import com.jcosta.tinybank.domain.exceptions.BusinessException;
import com.jcosta.tinybank.domain.exceptions.ExceptionCode;
import com.jcosta.tinybank.domain.users.User;
import com.jcosta.tinybank.domain.users.UserStatus;
import org.jboss.logging.Logger;

import static io.quarkiverse.loggingjson.providers.KeyValueStructuredArgument.kv;

public class CreateUser {
    private final UserDataService userDataService;

    public CreateUser(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    public User execute(CreateUserRequest createUserRequest) {
        Search<User> existing = this.userDataService.search(
                createUserRequest.username(),
                null,
                null,
                true);

        if (existing.getItems().size() > 0) {
            throw new BusinessException(ExceptionCode.DUPLICATE_EXCEPTION, "username already being used");
        }

        return this.userDataService.create(new User(null, createUserRequest.username(), UserStatus.ACTIVE));
    }
}
