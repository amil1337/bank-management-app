package org.example.repository;

import org.example.model.Account;

import java.util.*;

public class InMemoryAccountRepository implements AccountRepository {
    private final Map<String, Account> accounts = new HashMap<>();

    @Override
    public void save(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return Optional.ofNullable(accounts.get(accountNumber));
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public void deleteByAccountNumber(String accountNumber) {
        accounts.remove(accountNumber);
    }

    @Override
    public void save(Account account, String userEmail) {
        save(account);
    }
}