package com.jcosta.tinybank.config.di;

import com.jcosta.tinybank.application.usecases.users.CreateUser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;

public class DependencyInjectionConfig {
    @Produces
    @ApplicationScoped
    CreateUser produceCreateUser() {
        return new CreateUser();
    }
}
