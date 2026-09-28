package org.example.cli;

import org.example.model.*;
import org.example.service.BankService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class BankCLI {
    private final BankService bankService;
    private final Scanner scanner;

    public BankCLI(BankService bankService) {
        this.bankService = bankService;
        scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("""
                    1 - Create User
                    2 - Create Account
                    3 - Show Accounts
                    4 - Show Balance
                    5 - Deposit
                    6 - Withdraw
                    7 - Transfer
                    8 - Delete Account
                    9 - Transaction History
                    10 - Show Users
                    11 - Show User Accounts
                    0 - Exit
                    """);

            System.out.print("Choose: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> createUser();
                case 2 -> createAccount();
                case 3 -> showAccounts();
                case 4 -> showBalance();
                case 5 -> deposit();
                case 6 -> withdraw();
                case 7 -> transfer();
                case 8 -> deleteAccount();
                case 9 -> getHistory();
                case 10 -> showUsers();
                case 11 -> showUserAccounts();
                case 0 -> {
                    System.out.println("Bye");
                    return;
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    public void createUser() {
        System.out.println("Your full name: ");
        String fullName = scanner.nextLine();

        System.out.println("Your email address: ");
        String email = scanner.nextLine();

        User user = new User(fullName, email);
        bankService.createUser(user);
        System.out.println("User created successfully. ID: " + user.getId());
    }

    public void createAccount() {
        System.out.println("Currency (1: AZN, 2: EUR, 3: USD): ");
        int currencyType = scanner.nextInt();
        System.out.println("Account type (1: Credit, 2: Savings, 3: Current): ");
        int accountType = scanner.nextInt();
        Currency currency;
        switch (currencyType) {
            case 1 -> currency = Currency.AZN;
            case 2 -> currency = Currency.EUR;
            case 3 -> currency = Currency.USD;
            default -> {
                System.out.println("Invalid currency");
                return;
            }
        }
        Account account;
        switch (accountType) {
            case 1 -> {
                System.out.print("Credit limit: ");
                BigDecimal creditLimit = scanner.nextBigDecimal();
                account = new CreditAccount(BigDecimal.ZERO, currency, creditLimit);
            }
            case 2 -> {
                account = new SavingsAccount(BigDecimal.ZERO, currency, new BigDecimal("0.05"));
            }
            case 3 -> {
                account = new CurrentAccount(BigDecimal.ZERO, currency, new BigDecimal("200"));
            }
            default -> {
                System.out.println("Invalid account type");
                return;
            }
        }
        System.out.print("Enter your mail address: ");
        scanner.nextLine();
        String mail = scanner.nextLine();

        bankService.createAccount(mail, account);

        System.out.println("Account created successfully!");
        System.out.println("Account number: " + account.getAccountNumber());
    }

    public void showAccounts() {
        List<Account> accounts = bankService.getAllAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts found");
            return;
        }
        for (Account account : accounts) {
            System.out.println("--------------------");
            System.out.println("Owner email: " + bankService.getAccountOwnerEmail(account.getAccountNumber()));
            System.out.println("Account number: " + account.getAccountNumber());
            System.out.println("Account type: " + account.getAccountType());
            System.out.println("Balance: " + account.getBalance() + " " + account.getCurrency());
        }
    }

    public void showUsers() {
        List<User> users = bankService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found");
            return;
        }

        for (User user : users) {
            System.out.println("--------------------");
            System.out.println("ID: " + user.getId());
            System.out.println("Full name: " + user.getFullName());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Accounts count: " + user.getAccountsList().size());
        }
    }

    public void showUserAccounts() {
        System.out.print("User email: ");
        String email = scanner.nextLine();

        List<Account> accounts = bankService.getAccountsByUserEmail(email);
        if (accounts.isEmpty()) {
            System.out.println("This user has no accounts");
            return;
        }

        for (Account account : accounts) {
            System.out.println("--------------------");
            System.out.println("Account number: " + account.getAccountNumber());
            System.out.println("Account type: " + account.getAccountType());
            System.out.println("Balance: " + account.getBalance() + " " + account.getCurrency());
        }
    }

    public void showBalance() {
        System.out.print("Account number: ");
        String accountNumber = scanner.nextLine();

        Account account = bankService.getAccount(accountNumber);
        System.out.println("Balance: " + account.getBalance() + " " + account.getCurrency());
    }

    public void deposit() {
        System.out.print("Account number: ");
        String accountNumber = scanner.nextLine();

        System.out.print("Amount: ");
        BigDecimal amount = new BigDecimal(scanner.nextLine());

        bankService.deposit(accountNumber, amount);
        System.out.println("Deposit successful");
    }

    public void withdraw() {
        System.out.print("Account number: ");
        String accountNumber = scanner.nextLine();

        System.out.print("Amount: ");
        BigDecimal amount = new BigDecimal(scanner.nextLine());

        bankService.withdraw(accountNumber, amount);
        System.out.println("Withdraw successful");
    }

    public void transfer() {
        System.out.print("Sender account: ");
        String sender = scanner.nextLine();

        System.out.print("Receiver account: ");
        String receiver = scanner.nextLine();

        System.out.print("Amount: ");
        BigDecimal amount = new BigDecimal(scanner.nextLine());

        bankService.transfer(sender, receiver, amount);
        System.out.println("Transfer successful");
    }

    public void deleteAccount() {
        System.out.print("Account number: ");
        String accountNumber = scanner.nextLine();
        bankService.deleteAccount(accountNumber);
        System.out.println("Account deleted successfully");
    }

    public void getHistory() {
        var transactions = bankService.getTransactionHistory();
        if (transactions.isEmpty()) {
            System.out.println("No transactions found");
        } else {
            for (Transaction transaction : transactions) {
                System.out.println("--------------------");
                System.out.println("ID: " + transaction.getId());
                System.out.println("Type: " + transaction.getTransactionType());
                System.out.println("From: " + transaction.getSourceAccountNumber());
                System.out.println("To: " + transaction.getTargetAccountNumber());
                System.out.println("Amount: " + transaction.getAmount() + " " + transaction.getSourceCurrency());
                if (transaction.getTransactionType() == TransactionType.TRANSFER) {
                    System.out.println("Converted amount: " + transaction.getTargetAmount() + " " + transaction.getTargetCurrency());
                }
                System.out.println("Date: " + transaction.getTimestamp());
            }
        }
    }
}
