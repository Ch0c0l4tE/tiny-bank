package com.jcosta.tinybank.adapters.in.web.users;

import com.github.fge.jsonpatch.JsonPatch;
import com.jcosta.tinybank.adapters.in.web.hal.Link;
import com.jcosta.tinybank.adapters.in.web.hal.ListWebResponse;
import com.jcosta.tinybank.adapters.in.web.hal.WebResponse;
import com.jcosta.tinybank.application.usecases.users.CreateUser;
import com.jcosta.tinybank.application.usecases.users.CreateUserRequest;
import com.jcosta.tinybank.application.usecases.users.GetUserById;
import com.jcosta.tinybank.application.usecases.users.PatchUser;
import com.jcosta.tinybank.application.usecases.users.SearchUsers;
import com.jcosta.tinybank.application.usecases.users.UserSearchQuery;
import com.jcosta.tinybank.domain.Search;
import com.jcosta.tinybank.domain.users.User;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Path("/users")
public class UsersResource {

    private final CreateUser createUser;
    private final GetUserById getUserById;
    private final PatchUser patchUser;
    private final SearchUsers searchUsers;

    public UsersResource(
            CreateUser createUser,
            GetUserById getUserById,
            PatchUser patchUser,
            SearchUsers searchUsers
    ) {
        this.createUser = createUser;
        this.getUserById = getUserById;
        this.patchUser = patchUser;
        this.searchUsers = searchUsers;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(@Context UriInfo uriInfo, CreateUserRequest request) {
        return Response.ok(toWebResponse(createUser.execute(request), uriInfo.getBaseUri())).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response search(@Context UriInfo uriInfo, @QueryParam("username") String username, @QueryParam("limit") Integer limit, @QueryParam("cursor") String cursor) {
        return Response.ok(toListWebResponse(searchUsers.execute(new UserSearchQuery(username, limit, cursor)), uriInfo.getBaseUri())).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@Context UriInfo uriInfo, String id){
        return Response.ok(toWebResponse(getUserById.execute(id), uriInfo.getBaseUri())).build();
    }

    @PATCH
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response patch(String id, JsonPatch patch) {
        this.patchUser.execute(id, patch);
        return Response.noContent().build();
    }

    private WebResponse<User> toWebResponse(User user, URI baseUri) {
        Map<String, Link> links = new HashMap<>();

        links.put("patch",
                new Link(UriBuilder.fromUri(baseUri)
                        .path("users")
                        .path(user.id()).build().toString(), HttpMethod.PATCH));

        return new WebResponse<>(user, links);
    }

    private ListWebResponse<WebResponse<User>> toListWebResponse(Search<User> search, URI baseUri) {
        Map<String, Link> links = new HashMap<>();

        String cursor = search.getCursor();

        if (cursor != null) {
            links.put("next",
                    new Link(UriBuilder.fromUri(baseUri)
                            .path("users")
                            .queryParam("limit", search.getLimit())
                            .queryParam("cursor", search.getCursor()).build().toString(), HttpMethod.GET ));
        }

        return new ListWebResponse<>(
                search.getItems().stream().map(user -> toWebResponse(user, baseUri)).toList(),
                links);
    }
}
