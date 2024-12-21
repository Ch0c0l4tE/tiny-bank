package com.jcosta.tinybank.adapters.in.web.users;

import com.jcosta.tinybank.application.usecases.users.CreateUser;
import com.jcosta.tinybank.application.usecases.users.CreateUserRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/users")
public class UsersResource {

    private final CreateUser createUser;

    public UsersResource(CreateUser createUser) {
        this.createUser = createUser;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String post(CreateUserRequest request) {
        createUser.execute();
        return "Hello from Quarkus REST";
    }
}
