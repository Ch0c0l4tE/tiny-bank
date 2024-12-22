package com.jcosta.tinybank.adapters.in.web.users;

import com.github.fge.jsonpatch.JsonPatch;
import com.jcosta.tinybank.adapters.in.web.hal.Link;
import com.jcosta.tinybank.adapters.in.web.hal.WebResponse;
import com.jcosta.tinybank.application.usecases.users.CreateUser;
import com.jcosta.tinybank.application.usecases.users.CreateUserRequest;
import com.jcosta.tinybank.application.usecases.users.GetUserById;
import com.jcosta.tinybank.application.usecases.users.PatchUser;
import com.jcosta.tinybank.domain.users.User;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
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
    private final GetUserById getUserById;
    private final PatchUser patchUser;

    public UsersResource(
            CreateUser createUser,
            GetUserById getUserById,
            PatchUser patchUser
    ) {
        this.createUser = createUser;
        this.getUserById = getUserById;
        this.patchUser = patchUser;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(CreateUserRequest request) {
        return Response.ok(toWebResponse(createUser.execute(request))).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(String id){
        return Response.ok(toWebResponse(getUserById.execute(id))).build();
    }

    @PATCH
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response patch(String id, JsonPatch patch) {
        this.patchUser.execute(id, patch);
        return Response.noContent().build();
    }

    private WebResponse<User> toWebResponse(User user) {
        Map<String, Link> links = new HashMap<>();
        return new WebResponse<>(user, links);
    }
}
