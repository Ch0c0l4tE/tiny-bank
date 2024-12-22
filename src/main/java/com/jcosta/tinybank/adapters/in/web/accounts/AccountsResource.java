package com.jcosta.tinybank.adapters.in.web.accounts;

import com.jcosta.tinybank.adapters.in.web.hal.Link;
import com.jcosta.tinybank.adapters.in.web.hal.ListWebResponse;
import com.jcosta.tinybank.adapters.in.web.hal.WebResponse;
import com.jcosta.tinybank.application.usecases.accounts.AccountsSearchQuery;
import com.jcosta.tinybank.application.usecases.accounts.CreateAccount;
import com.jcosta.tinybank.application.usecases.accounts.CreateAccountRequest;
import com.jcosta.tinybank.application.usecases.accounts.GetAccountById;
import com.jcosta.tinybank.application.usecases.accounts.SearchAccounts;
import com.jcosta.tinybank.domain.Search;
import com.jcosta.tinybank.domain.accounts.Account;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HttpMethod;
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

@Path("accounts")
public class AccountsResource {

    private final CreateAccount createAccount;
    private final GetAccountById getAccountById;
    private final SearchAccounts searchAccounts;

    public AccountsResource(
            CreateAccount createAccount,
            GetAccountById getAccountById,
            SearchAccounts searchAccounts
    ){

        this.createAccount = createAccount;
        this.getAccountById = getAccountById;
        this.searchAccounts = searchAccounts;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(@Context UriInfo uriInfo, CreateAccountRequest request) {
        return Response.ok(toWebResponse(createAccount.execute(request), uriInfo.getBaseUri(), false)).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response search(@Context UriInfo uriInfo, @QueryParam("ownerId") String ownerId, @QueryParam("limit") Integer limit, @QueryParam("cursor") String cursor) {
        AccountsSearchQuery query = new AccountsSearchQuery(ownerId, limit, cursor);
        return Response.ok(toListWebResponse(searchAccounts.execute(query), uriInfo.getBaseUri(), query)).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@Context UriInfo uriInfo, String id){
        return Response.ok(toWebResponse(getAccountById.execute(id), uriInfo.getBaseUri(), false)).build();
    }

    private WebResponse<Account> toWebResponse(Account account, URI baseUri, boolean includeSelf) {
        Map<String, Link> links = new HashMap<>();

        if (includeSelf) {
            links.put("self",
                    new Link(UriBuilder.fromUri(baseUri)
                            .path("accounts")
                            .path(account.id())
                            .build().toString(), HttpMethod.GET));
        }

        return new WebResponse<>(account, links);
    }

    private ListWebResponse<WebResponse<Account>> toListWebResponse(Search<Account> search, URI baseUri, AccountsSearchQuery query) {
        Map<String, Link> links = new HashMap<>();

        String cursor = search.getCursor();

        if (cursor != null) {
            UriBuilder builder = UriBuilder.fromUri(baseUri)
                    .path("accounts")
                    .queryParam("limit", search.getLimit())
                    .queryParam("cursor", search.getCursor());


            if(query.ownerId() != null && !query.ownerId().isEmpty()) {
                builder.queryParam("ownerId", query.ownerId());
            }

            links.put("next",
                    new Link(builder.build().toString(), HttpMethod.GET));
        }

        return new ListWebResponse<>(
                search.getItems().stream().map(item -> toWebResponse(item, baseUri, true)).toList(),
                links);
    }
}
