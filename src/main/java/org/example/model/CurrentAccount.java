package org.example.model;

import org.example.exception.InsufficientFundsException;

import java.math.BigDecimal;

public class CurrentAccount extends BaseAccount {
    private final BigDecimal overdraftLimit;

    public CurrentAccount(BigDecimal balance, Currency currency, BigDecimal overdraftLimit) {
        super(balance, currency);
        this.overdraftLimit = overdraftLimit;
    }

    public CurrentAccount(String accountNumber, BigDecimal balance, Currency currency, BigDecimal overdraftLimit) {
        super(accountNumber, balance, currency);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(BigDecimal amount) {
        BigDecimal availableAmount = getBalance().add(overdraftLimit);
        if (amount.compareTo(availableAmount) > 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        setBalance(getBalance().subtract(amount));
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.CURRENT;
    }
}
