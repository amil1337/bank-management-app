package org.example.model;

import org.example.exception.InsufficientFundsException;

import java.math.BigDecimal;

public class SavingsAccount extends BaseAccount {
    private final BigDecimal interestRate;

    public SavingsAccount(BigDecimal balance, Currency currency, BigDecimal interestRate) {
        super(balance, currency);
        this.interestRate = interestRate;
    }

    public SavingsAccount(String accountNumber, BigDecimal balance, Currency currency, BigDecimal interestRate) {
        super(accountNumber, balance, currency);
        this.interestRate = interestRate;
    }

    public void applyInterest(){
        BigDecimal interest = getBalance().multiply(interestRate);
        setBalance(getBalance().add(interest));
    }

    @Override
    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(getBalance()) > 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        setBalance(getBalance().subtract(amount));
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.SAVINGS;
    }
}
