package com.jcosta.tinybank.application.port;

import com.jcosta.tinybank.domain.Search;
import com.jcosta.tinybank.domain.users.User;

import java.util.UUID;

public interface UserDataService {
    User create(User user);

    User get(String id);

    Search<User> search(String username, int limit, String cursor);

    void update(User user);
}
