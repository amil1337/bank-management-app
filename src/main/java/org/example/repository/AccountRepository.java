package org.example.repository;

import org.example.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    void save(Account account);

    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findAll();

    void deleteByAccountNumber(String accountNumber);

    void save(Account account, String userEmail);
}
