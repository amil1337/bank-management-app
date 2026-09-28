package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private final UUID id;
    private final String sourceAccountNumber;
    private final String targetAccountNumber;
    private final BigDecimal amount;
    private final BigDecimal targetAmount;
    private final Currency sourceCurrency;
    private final Currency targetCurrency;
    private final LocalDateTime timestamp;
    private final TransactionType transactionType;

    public Transaction(String sourceAccountNumber, String targetAccountNumber, BigDecimal amount, BigDecimal targetAmount, Currency sourceCurrency, Currency targetCurrency, TransactionType transactionType) {
        id = UUID.randomUUID();
        this.sourceAccountNumber = sourceAccountNumber;
        this.targetAccountNumber = targetAccountNumber;
        this.amount = amount;
        this.targetAmount = targetAmount;
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
        timestamp = LocalDateTime.now();
        this.transactionType = transactionType;
    }

    public Transaction(UUID id, String sourceAccountNumber, String targetAccountNumber, BigDecimal amount, BigDecimal targetAmount, Currency sourceCurrency, Currency targetCurrency, LocalDateTime timestamp, TransactionType transactionType) {
        this.id = id;
        this.sourceAccountNumber = sourceAccountNumber;
        this.targetAccountNumber = targetAccountNumber;
        this.amount = amount;
        this.targetAmount = targetAmount;
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
        this.timestamp = timestamp;
        this.transactionType = transactionType;
    }

    public static Transaction deposit(String accountNumber, BigDecimal amount, Currency currency) {
        return new Transaction(null, accountNumber, amount, amount, null, currency, TransactionType.DEPOSIT);
    }

    public static Transaction withdraw(String accountNumber, BigDecimal amount, Currency currency) {
        return new Transaction(accountNumber, null, amount, amount, currency, null, TransactionType.WITHDRAW);
    }

    public static Transaction transfer(String from, String to, BigDecimal amount, BigDecimal targetAmount, Currency sourceCurrency, Currency targetCurrency) {
        return new Transaction(from, to, amount, targetAmount, sourceCurrency, targetCurrency, TransactionType.TRANSFER);
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public Currency getSourceCurrency() {
        return sourceCurrency;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public String getTargetAccountNumber() {
        return targetAccountNumber;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
