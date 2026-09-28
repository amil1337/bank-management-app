package org.example.model;

import java.math.BigDecimal;

public interface Account {
    String getAccountNumber();

    BigDecimal getBalance();

    void deposit(BigDecimal amount);

    void withdraw(BigDecimal amount);

    AccountType getAccountType();

    Currency getCurrency();
}
