package com.jcosta.tinybank.application.usecases.users;

import com.jcosta.tinybank.domain.users.User;
import org.jboss.logging.Logger;

import static io.quarkiverse.loggingjson.providers.KeyValueStructuredArgument.kv;

public class CreateUser {

    private static final Logger LOG = Logger.getLogger(CreateUser.class);

    public User execute() {
        LOG.infof("message:", kv("joao", new User("test logger with extre key and values as object")));
        return null;
    }
}
