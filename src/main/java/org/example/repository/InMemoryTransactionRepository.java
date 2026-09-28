package org.example.repository;

import org.example.model.Transaction;

import java.util.*;

public class InMemoryTransactionRepository implements TransactionRepository {
    private final Map<UUID, Transaction> transactions = new HashMap<>();

    @Override
    public void save(Transaction transaction) {
        transactions.put(transaction.getId(), transaction);
    }

    @Override
    public Optional<Transaction> findByTransactionId(UUID transactionId) {
        return Optional.ofNullable(transactions.get(transactionId));
    }

    @Override
    public List<Transaction> findAll() {
        return new ArrayList<>(transactions.values());
    }
}
