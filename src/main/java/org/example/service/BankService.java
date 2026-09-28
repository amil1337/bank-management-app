package org.example.service;

import org.example.exception.AccountNotFoundException;
import org.example.exception.InvalidAmountException;
import org.example.exception.InvalidTransferException;
import org.example.exception.UserNotFoundException;
import org.example.model.Account;
import org.example.model.CurrencyConverter;
import org.example.model.Transaction;
import org.example.model.User;
import org.example.repository.AccountRepository;
import org.example.repository.TransactionRepository;
import org.example.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;

public class BankService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CurrencyConverter currencyConverter;

    public BankService(AccountRepository accountRepository, TransactionRepository transactionRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        currencyConverter = new CurrencyConverter();
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public void createAccount(String email, Account account) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

        accountRepository.save(account, email);

        user.addAccount(account.getAccountNumber());

        userRepository.save(user);
    }

    public void deposit(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new AccountNotFoundException("Account not found"));
        account.deposit(amount);
        accountRepository.save(account);
        Transaction transaction = Transaction.deposit(accountNumber, amount, account.getCurrency());
        transactionRepository.save(transaction);
    }

    public void withdraw(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new AccountNotFoundException("Account not found"));
        account.withdraw(amount);
        accountRepository.save(account);
        Transaction transaction = Transaction.withdraw(accountNumber, amount, account.getCurrency());
        transactionRepository.save(transaction);
    }

    public void transfer(String fromAccNumber, String toAccNumber, BigDecimal amount) {
        Account sender = accountRepository.findByAccountNumber(fromAccNumber).orElseThrow(() -> new AccountNotFoundException("Account not found"));
        Account receiver = accountRepository.findByAccountNumber(toAccNumber).orElseThrow(() -> new AccountNotFoundException("Account not found"));
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than 0");
        }
        if (sender.getAccountNumber().equals(receiver.getAccountNumber())) {
            throw new InvalidTransferException("You can't send money to yourself");
        }
        BigDecimal convertedAmount = currencyConverter.convert(amount, sender.getCurrency(), receiver.getCurrency());
        sender.withdraw(amount);
        receiver.deposit(convertedAmount);
        accountRepository.save(sender);
        accountRepository.save(receiver);
        Transaction transaction = Transaction.transfer(fromAccNumber, toAccNumber, amount, convertedAmount, sender.getCurrency(), receiver.getCurrency());
        transactionRepository.save(transaction);
    }

    public BigDecimal getBalance(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new AccountNotFoundException("Account not found"));
        return account.getBalance();
    }

    public Account getAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Account> getAccountsByUserEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

        return user.getAccountsList().stream().map(this::getAccount).toList();
    }

    public String getAccountOwnerEmail(String accountNumber) {
        return userRepository.findAll().stream().filter(user -> user.getAccountsList().contains(accountNumber)).map(User::getEmail).findFirst().orElse("No owner");
    }

    public void deleteAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new AccountNotFoundException("Account not found"));

        accountRepository.deleteByAccountNumber(account.getAccountNumber());
        userRepository.findAll().forEach(user -> {
            user.removeAccount(account.getAccountNumber());
            userRepository.save(user);
        });
    }

    public List<Transaction> getTransactionHistory() {
        return transactionRepository.findAll();
    }
}

