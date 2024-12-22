package com.jcosta.tinybank.application.port;

import com.jcosta.tinybank.domain.Search;
import com.jcosta.tinybank.domain.users.User;

import java.util.UUID;

public interface UserDataService {
    User create(User user);

    User get(String id, boolean includeInactiveUsers);

    Search<User> search(String username, Integer limit, String cursor, boolean includeInactiveUsers);

    boolean update(User user);
}
