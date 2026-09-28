package org.example;

import org.example.cli.BankCLI;
import org.example.repository.*;
import org.example.service.BankService;

public class Main {
    public static void main(String[] args) {
        AccountRepository accountRepository = new JdbcAccountRepository();

        TransactionRepository transactionRepository = new JdbcTransactionRepository();

        UserRepository userRepository = new JdbcUserRepository();

        BankService bankService = new BankService(accountRepository, transactionRepository, userRepository);

        BankCLI bankCLI = new BankCLI(bankService);

        bankCLI.start();
    }
}