package com.jcosta.tinybank.adapters.in.web.users;

import com.jcosta.tinybank.adapters.in.web.hal.Link;
import com.jcosta.tinybank.adapters.in.web.hal.WebResponse;
import com.jcosta.tinybank.application.usecases.users.CreateUser;
import com.jcosta.tinybank.application.usecases.users.CreateUserRequest;
import com.jcosta.tinybank.domain.users.User;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

@Path("/users")
public class UsersResource {

    private final CreateUser createUser;

    public UsersResource(CreateUser createUser) {
        this.createUser = createUser;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(CreateUserRequest request) {
        return Response.ok(toWebResponse(createUser.execute(request))).build();
    }

    private WebResponse<User> toWebResponse(User user) {
        Map<String, Link> links = new HashMap<>();
        return new WebResponse<>(user, links);
    }
}
