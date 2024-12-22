package com.jcosta.tinybank.config.di;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcosta.tinybank.adapters.out.storage.inmemory.UsersStorage;
import com.jcosta.tinybank.application.port.UserDataService;
import com.jcosta.tinybank.application.usecases.users.CreateUser;
import com.jcosta.tinybank.application.usecases.users.GetUserById;
import com.jcosta.tinybank.application.usecases.users.PatchUser;
import com.jcosta.tinybank.application.usecases.users.SearchUsers;
import jakarta.enterprise.context.ApplicationScoped;
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

    @Produces
    @ApplicationScoped
    GetUserById produceGetUser(UserDataService userDataService) {
        return new GetUserById(userDataService);
    }

    @Produces
    @ApplicationScoped
    PatchUser producePatchUser(UserDataService userDataService, ObjectMapper objectMapper) {
        return new PatchUser(userDataService, objectMapper);
    }

    @Produces
    @ApplicationScoped
    SearchUsers produceSearchUsers(UserDataService userDataService) {
        return new SearchUsers(userDataService);
    }
}
