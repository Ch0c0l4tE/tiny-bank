package com.jcosta.tinybank.config.di;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcosta.tinybank.adapters.out.storage.inmemory.UsersStorage;
import com.jcosta.tinybank.application.port.UserDataService;
import com.jcosta.tinybank.application.usecases.users.CreateUser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Produces;

public class DependencyInjectionConfig {

    // Adapters
    @Produces
    @ApplicationScoped
    UserDataService produceUserDataService() {
        return new UsersStorage();
    }

    // Application
    @Produces
    @ApplicationScoped
    CreateUser produceCreateUser(UserDataService userDataService) {
        return new CreateUser(userDataService);
    }
}
