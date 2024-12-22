package com.jcosta.tinybank.adapters.in.web.transactions;

import com.jcosta.tinybank.adapters.in.web.hal.Link;
import com.jcosta.tinybank.adapters.in.web.hal.ListWebResponse;
import com.jcosta.tinybank.adapters.in.web.hal.WebResponse;
import com.jcosta.tinybank.application.usecases.transactions.CreateTransaction;
import com.jcosta.tinybank.application.usecases.transactions.CreateTransactionRequest;
import com.jcosta.tinybank.application.usecases.transactions.SearchTransactions;
import com.jcosta.tinybank.application.usecases.transactions.SearchTransactionsQuery;
import com.jcosta.tinybank.domain.Search;
import com.jcosta.tinybank.domain.transactions.Transaction;
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

@Path("/transactions")
public class TransactionsResource {

    private final CreateTransaction createTransaction;
    private final SearchTransactions searchTransactions;

    public TransactionsResource(CreateTransaction createTransaction, SearchTransactions searchTransactions) {
        this.createTransaction = createTransaction;
        this.searchTransactions = searchTransactions;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(@Context UriInfo uriInfo, CreateTransactionRequest request) {
        return Response.ok(toWebResponse(this.createTransaction.execute(request))).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response search(@Context UriInfo uriInfo, @QueryParam("accountId") String accountId, @QueryParam("limit") Integer limit, @QueryParam("cursor") String cursor) {
        SearchTransactionsQuery query = new SearchTransactionsQuery(accountId, limit, cursor);

        return Response.ok(toListWebResponse(this.searchTransactions.execute(query), uriInfo.getBaseUri(), query)).build();
    }

    private ListWebResponse<WebResponse<Transaction>> toListWebResponse(Search<Transaction> search, URI baseUri, SearchTransactionsQuery query) {
        Map<String, Link> links = new HashMap<>();

        String cursor = search.getCursor();

        if (cursor != null) {
            UriBuilder builder = UriBuilder.fromUri(baseUri)
                    .path("transactions")
                    .queryParam("limit", search.getLimit())
                    .queryParam("cursor", search.getCursor());

            if(query.accountId() != null && !query.accountId().isEmpty()){
                builder.queryParam("accountId", query.accountId());
            }

            links.put("next",
                    new Link(builder.build().toString(), HttpMethod.GET ));
        }

        return new ListWebResponse<>(
                search.getItems().stream().map(this::toWebResponse).toList(),
                links);
    }

    private WebResponse<Transaction> toWebResponse(Transaction transaction) {
        return new WebResponse<>(transaction, null);
    }
}
