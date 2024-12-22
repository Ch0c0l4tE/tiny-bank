package com.jcosta.tinybank.adapters.out.storage.inmemory;

import com.jcosta.tinybank.application.port.AccountsDataService;
import com.jcosta.tinybank.domain.Search;
import com.jcosta.tinybank.domain.accounts.Account;
import com.jcosta.tinybank.domain.accounts.AccountStatus;
import com.jcosta.tinybank.domain.exceptions.DomainException;
import com.jcosta.tinybank.domain.exceptions.ExceptionCode;
import com.jcosta.tinybank.domain.exceptions.InvalidParam;
import com.jcosta.tinybank.domain.users.User;
import com.jcosta.tinybank.domain.users.UserStatus;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.jcosta.tinybank.adapters.out.storage.inmemory.Crypto.decrypt;
import static com.jcosta.tinybank.adapters.out.storage.inmemory.Crypto.encrypt;

public class AccountsStorage implements AccountsDataService {
    private int MAX_LIMIT = 100;
    private int DEFAULT_LIMIT = 100;
    private static final ConcurrentHashMap<UUID, Account> db = new ConcurrentHashMap<>();

    @Override
    public Account create(Account account) {
        UUID id = IdHelper.generate();
        Account toCreate =  new Account(id.toString(), account.ownerId(), account.balance(), AccountStatus.ACTIVE);
        db.put(id,toCreate);
        return toCreate;
    }

    @Override
    public Account get(String id, boolean includeInactive) {
        UUID recordId = IdHelper.generateFromString(id);

        if (recordId == null) {
            return null;
        }

        Account existing =  db.get(recordId);

        if (existing == null) {
            return null;
        }

        if(!includeInactive && existing.status().equals(AccountStatus.INACTIVE)) {
            return null;
        }

        return existing;
    }

    @Override
    public Search<Account> search(String ownerId, Integer limit, String cursor, boolean includeInactive) {
        int validLimit;

        if (limit == null) {
            validLimit = DEFAULT_LIMIT;
        } else if (limit > MAX_LIMIT) {
            validLimit = MAX_LIMIT;
        } else {
            validLimit = limit;
        }


        long parsedCursor;

        if(cursor == null) {
            parsedCursor = 0;
        }else {
            try {
                parsedCursor = Long.parseLong(decrypt(cursor));
            } catch (NumberFormatException ex) {
                throw new DomainException(
                        ExceptionCode.VALIDATION_EXCEPTION,
                        new InvalidParam("cursor", "Invalid cursor"),
                        ex);
            }
        }

        boolean shouldIgnoreOwnerId = ownerId == null || ownerId.isEmpty();

        List<Account> users  = db
                .entrySet()
                .stream()
                .filter(userRecord -> {
                    return (shouldIgnoreOwnerId || userRecord.getValue().ownerId().equalsIgnoreCase(ownerId)) &&
                            (includeInactive || userRecord.getValue().status().equals(AccountStatus.ACTIVE));
                })
                .skip(parsedCursor)
                .limit(validLimit+1)
                .map(Map.Entry::getValue)
                .toList();

        String nextCursor = null;

        if (users.size() > validLimit){
            nextCursor = Long.toString(parsedCursor + validLimit);
            users = users.subList(0, users.size()-1);
        }

        return new Search<>(users, encrypt(nextCursor), validLimit);
    }

    @Override
    public boolean update(Account account) {
        UUID recordId = IdHelper.generateFromString(account.id());

        if(recordId == null || !db.containsKey(recordId)) {
            return false;
        }

        db.replace(recordId, account);
        return true;
    }
}
