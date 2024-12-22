package com.jcosta.tinybank.application.usecases.users;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.jcosta.tinybank.application.port.AccountsDataService;
import com.jcosta.tinybank.application.port.UserDataService;
import com.jcosta.tinybank.domain.Search;
import com.jcosta.tinybank.domain.accounts.Account;
import com.jcosta.tinybank.domain.accounts.AccountStatus;
import com.jcosta.tinybank.domain.exceptions.BusinessException;
import com.jcosta.tinybank.domain.exceptions.DomainException;
import com.jcosta.tinybank.domain.exceptions.ExceptionCode;
import com.jcosta.tinybank.domain.exceptions.InvalidParam;
import com.jcosta.tinybank.domain.users.User;
import com.jcosta.tinybank.domain.users.UserStatus;
import jakarta.json.JsonArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PatchUser {
    private final UserDataService userDataService;
    private final AccountsDataService accountsDataService;
    private final ObjectMapper objectMapper;

    public PatchUser(
            UserDataService userDataService,
            AccountsDataService accountsDataService,
            ObjectMapper objectMapper
    ) {
        this.userDataService = userDataService;
        this.accountsDataService = accountsDataService;
        this.objectMapper = objectMapper;
    }

    public boolean execute(String id, JsonPatch patch) {
        this.validate(patch);

        User user = this.userDataService.get(id, true);

        if (user == null) {
            throw new BusinessException(
                    ExceptionCode.NOT_FOUND_EXCEPTION,
                    String.format("user with id [%s] not fount", id));
        }

        try {
            JsonNode patchedUser = patch.apply(objectMapper.convertValue(user, JsonNode.class));
            user = objectMapper.convertValue(patchedUser, User.class);
        } catch (JsonPatchException e) {
            throw new BusinessException(
                    ExceptionCode.INTERNAL_SERVER_ERROR,
                    "Unable to process patch",
                    e);
        }

        Search<Account> userAccounts = null;

        AccountStatus status = AccountStatus.INACTIVE;
        if(user.status().toString().equalsIgnoreCase(UserStatus.ACTIVE.toString())) {
            status = AccountStatus.ACTIVE;
        }

        String cursor = null;
        do{
            userAccounts = this.accountsDataService.search(user.id(), 100, cursor, false);
            if (userAccounts != null && userAccounts.getItems().size() > 0) {
                for (Account account : userAccounts.getItems()) {
                    if(!account.status().toString().equalsIgnoreCase(status.toString())) {
                        this.accountsDataService.update(
                                new Account(
                                        account.id(),
                                        account.ownerId(),
                                        account.balance(),
                                        status));
                    }
                }
            }

            if(userAccounts == null) {
                cursor = null;
            }else {
                cursor = userAccounts.getCursor();
            }
        } while (cursor != null);

        return this.userDataService.update(user);
    }

    private final List<String> ALLOWED_PATHS = List.of("/status");
    private final List<String> ALLOWED_OPERATIONS = List.of("replace");

    public void validate(JsonPatch patch) {
        List<InvalidParam> errors = new ArrayList<>();

        JsonNode patchArrayNode = objectMapper.convertValue(patch, JsonNode.class);

        for (JsonNode operation : patchArrayNode) {
            String op = operation.get("op").asText();
            if (!ALLOWED_OPERATIONS.contains(op)) {
                String reason = "Invalid operation, valid operations: " + String.join(",", ALLOWED_OPERATIONS);
                errors.add(new InvalidParam(op, reason));
            }

            String path = operation.get("path").asText();
            if (!ALLOWED_PATHS.contains(path)) {
                String reason = "Invalid path, valid paths: " + String.join(",", ALLOWED_PATHS);
                errors.add(new InvalidParam(path, reason));
            }

            String value = operation.get("value").asText();
            try {
                UserStatus.valueOf(value);
            }catch (IllegalArgumentException ex) {
                String reason = "Invalid value option, valid value options: " +
                        String.join(",", Arrays.stream(UserStatus.values()).map(Enum::toString).collect(Collectors.joining(",")));
                errors.add(new InvalidParam(value, reason));
            }
        }

        if (errors.size() > 0) {
            throw new DomainException(
                    ExceptionCode.VALIDATION_EXCEPTION,
                    errors);
        }
    }
}
