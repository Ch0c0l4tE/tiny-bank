package com.jcosta.tinybank.adapters.out.storage.inmemory;

import com.jcosta.tinybank.application.port.UserDataService;
import com.jcosta.tinybank.domain.Search;
import com.jcosta.tinybank.domain.exceptions.BusinessException;
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

public class UsersStorage implements UserDataService {
    private final int MAX_LIMIT = 100;
    private final int DEFAULT_LIMIT = 100;
    private static final ConcurrentHashMap<UUID, User> usersDb = new ConcurrentHashMap<>();

    @Override
    public User create(User user) {
        UUID id = IdHelper.generate();
        User userToCreate =  new User(id.toString(), user.username(), UserStatus.ACTIVE);
        usersDb.put(id,userToCreate);
        return userToCreate;
    }

    @Override
    public User get(String id, boolean includeInactiveUsers) {
        UUID recordId = IdHelper.generateFromString(id);

        if (recordId == null) {
             return null;
        }

        User user =  usersDb.get(recordId);

        if (user == null) {
            return null;
        }

        if(!includeInactiveUsers && user.status().equals(UserStatus.INACTIVE)) {
            return null;
        }

        return user;
    }

    @Override
    public Search<User> search(String username, Integer limit, String cursor, boolean includeInactive) {
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

        boolean shouldIgnoreUsername = username == null || username.isEmpty();

        List<User> users  = usersDb
                .entrySet()
                .stream()
                .filter(userRecord -> {
                    return (shouldIgnoreUsername || userRecord.getValue().username().equalsIgnoreCase(username)) &&
                                    (includeInactive || userRecord.getValue().status().equals(UserStatus.ACTIVE));
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
    public boolean update(User user) {
        UUID recordId = IdHelper.generateFromString(user.id());

        if(recordId == null || !usersDb.containsKey(recordId)) {
            return false;
        }

        usersDb.replace(recordId, user);
        return true;
    }
}
