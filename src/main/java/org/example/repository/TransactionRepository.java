package org.example.repository;

import org.example.model.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    void save(Transaction transaction);

    Optional<Transaction> findByTransactionId(UUID transactionId);

    List<Transaction> findAll();
}
