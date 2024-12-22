package com.jcosta.tinybank.adapters.out.storage.inmemory;

import com.jcosta.tinybank.application.port.UserDataService;
import com.jcosta.tinybank.domain.Search;
import com.jcosta.tinybank.domain.exceptions.BusinessException;
import com.jcosta.tinybank.domain.exceptions.ExceptionCode;
import com.jcosta.tinybank.domain.users.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.jcosta.tinybank.adapters.out.storage.inmemory.Crypto.decrypt;
import static com.jcosta.tinybank.adapters.out.storage.inmemory.Crypto.encrypt;

public class UsersStorage implements UserDataService {

    private static final ConcurrentHashMap<UUID, User> usersDb = new ConcurrentHashMap<>();

    @Override
    public User create(User user) {
        UUID id = IdHelper.generate();
        usersDb.put(id, user);
        return new User(id.toString(), user.username());
    }

    @Override
    public User get(String id) {
        UUID recordId = IdHelper.generateFromString(id);

        if (recordId == null) {
            throw new BusinessException(
                    ExceptionCode.NOT_FOUND_EXCEPTION,
                    String.format("user with id [%s] not fount", id));
        }

        return usersDb.get(recordId);
    }

    @Override
    public Search<User> search(String username, int limit, String cursor) {
        long parsedCursor = Long.parseLong(decrypt(cursor));

        List<User> users  = usersDb
                .entrySet()
                .stream()
                .skip(parsedCursor)
                .filter(userRecord -> username == null || userRecord.getValue().username().equalsIgnoreCase(username))
                .map(Map.Entry::getValue)
                .toList();

        long nextCursor = parsedCursor + limit;
        return new Search<>(users, encrypt(Long.toString(nextCursor)));
    }

    @Override
    public void update(User user) {

        UUID recordId = IdHelper.generateFromString(user.id());

        if(recordId == null || !usersDb.containsKey(recordId)) {
            throw new BusinessException(ExceptionCode.NOT_FOUND_EXCEPTION, String.format("user with id [%s] not fount", user.id()));
        }

        usersDb.replace(recordId, user);
    }
}
