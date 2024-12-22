package com.jcosta.tinybank.adapters.out.storage.inmemory;

import com.jcosta.tinybank.application.port.TransactionsDataService;
import com.jcosta.tinybank.domain.Search;
import com.jcosta.tinybank.domain.exceptions.DomainException;
import com.jcosta.tinybank.domain.exceptions.ExceptionCode;
import com.jcosta.tinybank.domain.exceptions.InvalidParam;
import com.jcosta.tinybank.domain.transactions.Transaction;
import com.jcosta.tinybank.domain.users.User;
import com.jcosta.tinybank.domain.users.UserStatus;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import static com.jcosta.tinybank.adapters.out.storage.inmemory.Crypto.decrypt;
import static com.jcosta.tinybank.adapters.out.storage.inmemory.Crypto.encrypt;

public class TransactionsStorage implements TransactionsDataService {
    private final int MAX_LIMIT = 100;
    private final int DEFAULT_LIMIT = 100;

    private static final Comparator<Transaction> epochComparator = Comparator.comparingLong(Transaction::epochMillis);
    private static final TreeSet<Transaction> db = new TreeSet<>(epochComparator);

    @Override
    public Transaction create(Transaction transaction) {
        db.add(transaction);
        return transaction;
    }

    @Override
    public Search<Transaction> search(String accountId, Integer limit, String cursor) {
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


        List<Transaction> transactions  = db
                .stream()
                .filter(transaction -> {
                    return accountId == null || transaction.source().equalsIgnoreCase(accountId) || transaction.target().equalsIgnoreCase(accountId);
                })
                .skip(parsedCursor)
                .limit(validLimit+1)
                .toList();

        String nextCursor = null;

        if (transactions.size() > validLimit){
            nextCursor = Long.toString(parsedCursor + validLimit);
            transactions = transactions.subList(0, transactions.size()-1);
        }

        return new Search<>(transactions, encrypt(nextCursor), validLimit);
    }
}
