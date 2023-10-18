package io.ylab.walletservice.testutil;

import io.ylab.walletservice.exception.DatabaseException;
import io.ylab.walletservice.infrastructure.database.ConnectionManager;
import io.ylab.walletservice.util.PropertiesUtil;
import lombok.experimental.UtilityClass;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@UtilityClass
public class PostgresTestcontainer {

    public final PostgreSQLContainer<?> container = new PostgreSQLContainer<>(PropertiesUtil.get("db.test.imageVersion"));

    static {
        container.start();

        PropertiesUtil.replacePlaceholder("DB_URL", container.getJdbcUrl());
        PropertiesUtil.replacePlaceholder("DB_USERNAME", container.getUsername());
        PropertiesUtil.replacePlaceholder("DB_PASSWORD", container.getPassword());

        System.setProperty("app.environment", "test");
    }

    public void init() {
        ConnectionManager.reloadConfiguration();
        createTables();
    }

    public void close() {
        deleteTables();
        closePool();
    }

    private void closePool() {
        ConnectionManager.closeConnectionPool();
    }

    private void createTables() {
        String query = null;
        try {
            query = Files.readString(Paths.get("src/main/resources/tables.sql"));
            try (Connection connection = ConnectionManager.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.execute();
            } catch (DatabaseException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteTables() {
        try {
            try (Connection connection = ConnectionManager.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("DROP TABLE currencies, players, accounts, transactions;")) {
                preparedStatement.execute();
            } catch (DatabaseException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
