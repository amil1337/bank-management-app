package org.example.repository;

import org.example.config.DatabaseConnection;
import org.example.model.Currency;
import org.example.model.Transaction;
import org.example.model.TransactionType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JdbcTransactionRepository implements TransactionRepository {

    @Override
    public void save(Transaction transaction) {
        String sql = """
                insert into transactions (
                    id, source_account_number, target_account_number,
                    amount, target_amount, source_currency, target_currency,
                    transaction_type, created_at
                )
                values (?, ?, ?, ?, ?, ?, ?, ?, ?)
                on conflict (id) do nothing
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, transaction.getId());
            statement.setString(2, transaction.getSourceAccountNumber());
            statement.setString(3, transaction.getTargetAccountNumber());
            statement.setBigDecimal(4, transaction.getAmount());
            statement.setBigDecimal(5, transaction.getTargetAmount());

            if (transaction.getSourceCurrency() == null) {
                statement.setNull(6, Types.VARCHAR);
            } else {
                statement.setString(6, transaction.getSourceCurrency().name());
            }

            if (transaction.getTargetCurrency() == null) {
                statement.setNull(7, Types.VARCHAR);
            } else {
                statement.setString(7, transaction.getTargetCurrency().name());
            }

            statement.setString(8, transaction.getTransactionType().name());
            statement.setTimestamp(9, Timestamp.valueOf(transaction.getTimestamp()));

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save transaction", e);
        }
    }

    @Override
    public Optional<Transaction> findByTransactionId(UUID transactionId) {
        String sql = "select * from transactions where id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, transactionId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(mapTransaction(resultSet));
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find transaction", e);
        }
    }

    @Override
    public List<Transaction> findAll() {
        String sql = "select * from transactions order by created_at desc";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();

            List<Transaction> transactions = new ArrayList<>();

            while (resultSet.next()) {
                transactions.add(mapTransaction(resultSet));
            }

            return transactions;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find transactions", e);
        }
    }

    private Transaction mapTransaction(ResultSet resultSet) throws SQLException {
        Currency sourceCurrency = null;
        Currency targetCurrency = null;

        String sourceCurrencyValue = resultSet.getString("source_currency");
        String targetCurrencyValue = resultSet.getString("target_currency");

        if (sourceCurrencyValue != null) {
            sourceCurrency = Currency.valueOf(sourceCurrencyValue);
        }

        if (targetCurrencyValue != null) {
            targetCurrency = Currency.valueOf(targetCurrencyValue);
        }

        return new Transaction(
                resultSet.getObject("id", UUID.class),
                resultSet.getString("source_account_number"),
                resultSet.getString("target_account_number"),
                resultSet.getBigDecimal("amount"),
                resultSet.getBigDecimal("target_amount"),
                sourceCurrency,
                targetCurrency,
                resultSet.getTimestamp("created_at").toLocalDateTime(),
                TransactionType.valueOf(resultSet.getString("transaction_type"))
        );
    }
}