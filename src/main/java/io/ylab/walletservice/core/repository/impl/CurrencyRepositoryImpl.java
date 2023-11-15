package io.ylab.walletservice.core.repository.impl;

import io.ylab.walletservice.core.domain.Currency;
import io.ylab.walletservice.core.repository.CurrencyRepository;
import io.ylab.walletservice.exception.DatabaseException;
import io.ylab.walletservice.infrastructure.database.ConnectionManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The {@link CurrencyRepositoryImpl} class implements the {@code CurrencyRepository} interface
 * for accessing and manipulating currency entities in a repository using an in-memory map.
 *
 * @NoArgsConstructor Creates an instance of the class with a private no-argument constructor.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyRepositoryImpl implements CurrencyRepository<Long> {

    private static final CurrencyRepositoryImpl INSTANCE = new CurrencyRepositoryImpl();

    public static CurrencyRepositoryImpl getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Currency> findById(Long id) throws DatabaseException {
        String query = """
                SELECT * FROM currencies
                WHERE id=?;
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    ? Optional.of(buildCurrency(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Currency> findByCode(String code) throws DatabaseException {
        String query = """
                SELECT * FROM currencies
                WHERE code=?;
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    ? Optional.of(buildCurrency(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Currency> findAll() throws DatabaseException {
        String query = """
                SELECT * FROM currencies;
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Currency> currencies = new ArrayList<>();

            while (resultSet.next()) {
                currencies.add(buildCurrency(resultSet));
            }
            return currencies;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Currency save(Currency currency) throws DatabaseException {
        String query = """
                INSERT INTO currencies (code, rate)
                VALUES (?,?);
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setBigDecimal(2, currency.getRate());

            preparedStatement.executeUpdate();
            ResultSet keys = preparedStatement.getGeneratedKeys();

            if (keys.next()) {
                currency.setId(keys.getObject("id", Long.class));
            }

            return currency;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Currency currency) throws DatabaseException {
        String query = """
                UPDATE currencies
                SET code=?,
                    rate=?
                WHERE id=?;
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setBigDecimal(2, currency.getRate());
            preparedStatement.setLong(3, currency.getId());

            preparedStatement.executeUpdate();
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
                DELETE FROM currencies
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

    private static Currency buildCurrency(ResultSet resultSet) throws SQLException {
        return Currency.builder()
                .id(resultSet.getLong("id"))
                .code(resultSet.getString("code"))
                .rate(resultSet.getBigDecimal("rate"))
                .build();
    }
}

