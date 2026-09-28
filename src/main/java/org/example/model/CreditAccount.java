package org.example.model;

import org.example.exception.InsufficientFundsException;
import org.example.exception.InvalidAmountException;

import java.math.BigDecimal;

public class CreditAccount extends BaseAccount {
    private final BigDecimal creditLimit;

    public CreditAccount(BigDecimal balance, Currency currency, BigDecimal creditLimit) {
        super(balance, currency);
        this.creditLimit = creditLimit;
    }

    public CreditAccount(String accountNumber, BigDecimal balance, Currency currency, BigDecimal creditLimit) {
        super(accountNumber, balance, currency);
        this.creditLimit = creditLimit;
    }

    @Override
    public void withdraw(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than 0");
        }

        BigDecimal newBalance = getBalance().subtract(amount);

        if (newBalance.compareTo(creditLimit.negate()) < 0) {
            throw new InsufficientFundsException("Credit limit exceeded");
        }

        setBalance(newBalance);
    }

    @Override
    public void deposit(BigDecimal amount) {
        super.deposit(amount);
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.CREDIT;
    }
}