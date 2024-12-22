package com.jcosta.tinybank.application.usecases.users;

import com.jcosta.tinybank.application.port.UserDataService;
import com.jcosta.tinybank.domain.exceptions.BusinessException;
import com.jcosta.tinybank.domain.exceptions.ExceptionCode;
import com.jcosta.tinybank.domain.users.User;

public class GetUserById {
    private final UserDataService userDataService;

    public GetUserById(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    public User execute(String id) {
        User user = this.userDataService.get(id, true);

        if (user == null) {
            throw new BusinessException(
                    ExceptionCode.NOT_FOUND_EXCEPTION,
                    String.format("user with id [%s] not fount", id));
        }

        return user;
    }
}
