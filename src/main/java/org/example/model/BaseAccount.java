package org.example.model;

import org.example.exception.InvalidAmountException;

import java.math.BigDecimal;
import java.util.UUID;

public abstract class BaseAccount implements Account {
    private final String accountNumber;
    private BigDecimal balance;
    private final Currency currency;

    public BaseAccount(BigDecimal balance, Currency currency) {
        accountNumber = UUID.randomUUID().toString();
        this.balance = balance;
        this.currency = currency;
    }

    public BaseAccount(String accountNumber, BigDecimal balance, Currency currency) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.currency = currency;
    }

    @Override
    public void deposit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than 0");
        }
        balance = balance.add(amount);
    }

    @Override
    public abstract void withdraw(BigDecimal amount);

    @Override
    public String getAccountNumber() {
        return accountNumber;
    }

    @Override
    public BigDecimal getBalance() {
        return balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    protected void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}