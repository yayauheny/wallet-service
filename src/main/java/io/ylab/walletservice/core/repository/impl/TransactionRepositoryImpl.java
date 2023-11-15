package io.ylab.walletservice.core.repository.impl;

import io.ylab.walletservice.core.domain.Transaction;
import io.ylab.walletservice.core.domain.TransactionType;
import io.ylab.walletservice.core.repository.TransactionRepository;
import io.ylab.walletservice.exception.DatabaseException;
import io.ylab.walletservice.infrastructure.database.ConnectionManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The {@link TransactionRepositoryImpl} class implements the {@code TransactionRepository} interface
 * for accessing and manipulating transaction entities in a repository using an in-memory map.
 *
 * @NoArgsConstructor Creates an instance of the class with a private no-argument constructor.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionRepositoryImpl implements TransactionRepository<Long> {

    private static final TransactionRepositoryImpl INSTANCE = new TransactionRepositoryImpl();

    public static TransactionRepositoryImpl getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Transaction> findById(Long id) throws DatabaseException {
        String query = """
                SELECT * FROM transactions
                WHERE id=?;
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    ? Optional.of(buildTransaction(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transaction> findByPeriod(LocalDateTime from, LocalDateTime to, Long accountId) throws DatabaseException {
        String query = """
                SELECT * FROM transactions
                WHERE participant_account_id=?
                  AND (created_at > ? AND created_at < ?);
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, accountId);
            preparedStatement.setObject(2, from);
            preparedStatement.setObject(3, to);

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Transaction> transactions = new ArrayList<>();

            while (resultSet.next()) {
                transactions.add(buildTransaction(resultSet));
            }
            return transactions;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transaction> findAllByAccountId(Long accountId) throws DatabaseException {
        String query = """
                SELECT * FROM transactions
                WHERE participant_account_id=?;
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, accountId);

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Transaction> transactions = new ArrayList<>();

            while (resultSet.next()) {
                transactions.add(buildTransaction(resultSet));
            }
            return transactions;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transaction> findAll() throws DatabaseException {
        String query = """
                SELECT * FROM transactions;
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Transaction> transactions = new ArrayList<>();

            while (resultSet.next()) {
                transactions.add(buildTransaction(resultSet));
            }
            return transactions;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction save(Transaction transaction) throws DatabaseException {
        String query = """
                INSERT INTO transactions (type, amount, created_at, currency_code, participant_account_id)
                VALUES (?,?,?,?,?);
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, transaction.getType().name());
            preparedStatement.setBigDecimal(2, transaction.getAmount());
            preparedStatement.setObject(3, transaction.getCreatedAt());
            preparedStatement.setString(4, transaction.getCurrencyCode());
            preparedStatement.setLong(5, transaction.getParticipantAccountId());

            preparedStatement.executeUpdate();
            ResultSet keys = preparedStatement.getGeneratedKeys();

            if (keys.next()) {
                transaction.setId(keys.getObject("id", Long.class));
            }

            return transaction;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Long id) throws DatabaseException {
        String query = """
                DELETE FROM transactions
                WHERE id=?;
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            return !preparedStatement.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private static Transaction buildTransaction(ResultSet resultSet) throws SQLException {
        return Transaction.builder()
                .id(resultSet.getLong("id"))
                .type(TransactionType.valueOf(resultSet.getString("type")))
                .amount(resultSet.getBigDecimal("amount"))
                .createdAt(resultSet.getObject("created_at", LocalDateTime.class))
                .currencyCode(resultSet.getString("currency_code"))
                .participantAccountId(resultSet.getLong("participant_account_id"))
                .build();
    }
}

