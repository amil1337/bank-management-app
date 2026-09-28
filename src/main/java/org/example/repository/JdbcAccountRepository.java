package org.example.repository;

import org.example.config.DatabaseConnection;
import org.example.model.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcAccountRepository implements AccountRepository {

    @Override
    public void save(Account account) {
        String sql = """
                insert into accounts (
                    account_number, account_type, currency, balance,
                    credit_limit, overdraft_limit, interest_rate
                )
                values (?, ?, ?, ?, ?, ?, ?)
                on conflict (account_number) do update set
                    account_type = excluded.account_type,
                    currency = excluded.currency,
                    balance = excluded.balance,
                    credit_limit = excluded.credit_limit,
                    overdraft_limit = excluded.overdraft_limit,
                    interest_rate = excluded.interest_rate
                """;

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, account.getAccountNumber());
            statement.setString(2, account.getAccountType().name());
            statement.setString(3, account.getCurrency().name());
            statement.setBigDecimal(4, account.getBalance());

            if (account instanceof CreditAccount) {
                statement.setBigDecimal(5, new BigDecimal("500"));
                statement.setNull(6, Types.NUMERIC);
                statement.setNull(7, Types.NUMERIC);
            } else if (account instanceof CurrentAccount) {
                statement.setNull(5, Types.NUMERIC);
                statement.setBigDecimal(6, new BigDecimal("200"));
                statement.setNull(7, Types.NUMERIC);
            } else if (account instanceof SavingsAccount) {
                statement.setNull(5, Types.NUMERIC);
                statement.setNull(6, Types.NUMERIC);
                statement.setBigDecimal(7, new BigDecimal("0.05"));
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save account", e);
        }
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        String sql = "select * from accounts where account_number = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, accountNumber);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(mapAccount(resultSet));
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find account", e);
        }
    }

    @Override
    public List<Account> findAll() {
        String sql = "select * from accounts";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();

            List<Account> accounts = new ArrayList<>();

            while (resultSet.next()) {
                accounts.add(mapAccount(resultSet));
            }

            return accounts;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find accounts", e);
        }
    }

    @Override
    public void deleteByAccountNumber(String accountNumber) {
        String sql = "delete from accounts where account_number = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, accountNumber);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete account", e);
        }
    }

    private Account mapAccount(ResultSet resultSet) throws SQLException {
        String accountNumber = resultSet.getString("account_number");
        AccountType accountType = AccountType.valueOf(resultSet.getString("account_type"));
        Currency currency = Currency.valueOf(resultSet.getString("currency"));
        BigDecimal balance = resultSet.getBigDecimal("balance");

        return switch (accountType) {
            case CREDIT -> new CreditAccount(accountNumber, balance, currency, resultSet.getBigDecimal("credit_limit"));
            case CURRENT ->
                    new CurrentAccount(accountNumber, balance, currency, resultSet.getBigDecimal("overdraft_limit"));
            case SAVINGS ->
                    new SavingsAccount(accountNumber, balance, currency, resultSet.getBigDecimal("interest_rate"));
        };
    }

    @Override
    public void save(Account account, String userEmail) {
        String sql = """
                insert into accounts (
                    account_number, user_email, account_type, currency, balance,
                    credit_limit, overdraft_limit, interest_rate
                )
                values (?, ?, ?, ?, ?, ?, ?, ?)
                on conflict (account_number) do update set
                    user_email = excluded.user_email,
                    account_type = excluded.account_type,
                    currency = excluded.currency,
                    balance = excluded.balance,
                    credit_limit = excluded.credit_limit,
                    overdraft_limit = excluded.overdraft_limit,
                    interest_rate = excluded.interest_rate
                """;

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, account.getAccountNumber());
            statement.setString(2, userEmail);
            statement.setString(3, account.getAccountType().name());
            statement.setString(4, account.getCurrency().name());
            statement.setBigDecimal(5, account.getBalance());

            if (account instanceof CreditAccount) {
                statement.setBigDecimal(6, new BigDecimal("500"));
                statement.setNull(7, Types.NUMERIC);
                statement.setNull(8, Types.NUMERIC);
            } else if (account instanceof CurrentAccount) {
                statement.setNull(6, Types.NUMERIC);
                statement.setBigDecimal(7, new BigDecimal("200"));
                statement.setNull(8, Types.NUMERIC);
            } else if (account instanceof SavingsAccount) {
                statement.setNull(6, Types.NUMERIC);
                statement.setNull(7, Types.NUMERIC);
                statement.setBigDecimal(8, new BigDecimal("0.05"));
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save account", e);
        }
    }
}