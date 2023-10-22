package io.ylab.walletservice.core.repository.impl;

import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.domain.Transaction;
import io.ylab.walletservice.core.domain.TransactionType;
import io.ylab.walletservice.core.repository.AccountRepository;
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
 * The {@link AccountRepositoryImpl} class implements the {@code AccountRepository} interface
 * for accessing and manipulating account entities in a repository using an in-memory map.
 *
 * @NoArgsConstructor Creates an instance of the class with a private no-argument constructor.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountRepositoryImpl implements AccountRepository<Long> {

    private static final AccountRepositoryImpl INSTANCE = new AccountRepositoryImpl();

    public static AccountRepositoryImpl getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Account> findById(Long id) throws DatabaseException {
        String query = """
                SELECT * FROM accounts
                WHERE id=?;
                """;
        return findByIdTemplate(query, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Account> findByPlayerId(Long playerId) throws DatabaseException {
        String query = """
                SELECT * FROM accounts
                WHERE player_id=?;
                """;
        return findByIdTemplate(query, playerId);
    }

    private Optional<Account> findByIdTemplate(String query, Long id) throws DatabaseException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    ? Optional.of(buildAccount(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Account> findAll() throws DatabaseException {
        String query = """
                SELECT * FROM accounts;
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Account> accounts = new ArrayList<>();

            while (resultSet.next()) {
                accounts.add(buildAccount(resultSet));
            }
            return accounts;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public List<Transaction> findAllTransactions(Long accountId) throws DatabaseException {
        String query = """
                SELECT * FROM transactions
                WHERE participant_account_id=?
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, accountId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Transaction> transactions = new ArrayList<>();

            while (resultSet.next()) {
                transactions.add(Transaction.builder()
                        .id(resultSet.getLong("id"))
                        .type(TransactionType.valueOf(resultSet.getString("type")))
                        .amount(resultSet.getBigDecimal("amount"))
                        .createdAt(resultSet.getObject("created_at", LocalDateTime.class))
                        .currencyCode(resultSet.getString("currency_code"))
                        .participantAccountId(resultSet.getLong("participant_account_id"))
                        .build()
                );
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
    public Account save(Account account) throws DatabaseException {
        String query = """
                INSERT INTO accounts (current_balance, created_at, currency_code, player_id)
                VALUES (?,?,?,?);
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setBigDecimal(1, account.getCurrentBalance());
            preparedStatement.setObject(2, account.getCreatedAt());
            preparedStatement.setString(3, account.getCurrencyCode());
            preparedStatement.setLong(4, account.getPlayerId());

            preparedStatement.executeUpdate();
            ResultSet keys = preparedStatement.getGeneratedKeys();

            if (keys.next()) {
                account.setId(keys.getObject("id", Long.class));
            }

            return account;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Account account) throws DatabaseException {
        String query = """
                UPDATE accounts
                SET current_balance=?,
                    created_at=?,
                    currency_code=?,
                    player_id=?
                WHERE id=?;
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setBigDecimal(1, account.getCurrentBalance());
            preparedStatement.setObject(2, account.getCreatedAt());
            preparedStatement.setString(3, account.getCurrencyCode());
            preparedStatement.setLong(4, account.getPlayerId());
            preparedStatement.setLong(5, account.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Account account) throws DatabaseException {
        String query = """
                DELETE FROM accounts
                WHERE id=?;
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, account.getId());
            return !preparedStatement.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private static Account buildAccount(ResultSet resultSet) throws SQLException {
        return Account.builder()
                .id(resultSet.getLong("id"))
                .currentBalance(resultSet.getBigDecimal("current_balance"))
                .createdAt(resultSet.getObject("created_at", LocalDateTime.class))
                .currencyCode(resultSet.getString("currency_code"))
                .playerId(resultSet.getLong("player_id"))
                .build();
    }
}

