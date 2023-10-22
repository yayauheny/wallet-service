package io.ylab.walletservice.testutil;

import io.ylab.walletservice.exception.DatabaseException;
import io.ylab.walletservice.infrastructure.database.ConnectionManager;
import io.ylab.walletservice.infrastructure.database.LiquibaseMigration;
import io.ylab.walletservice.util.PropertiesUtil;
import lombok.experimental.UtilityClass;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * The {@code PostgresTestcontainer} class provides utility methods for managing a PostgreSQL Docker container
 * during tests. It is designed to handle the initialization and cleanup of the test environment,
 * including starting and stopping the Docker container, configuring database connection properties, and applying
 * Liquibase migrations.
 *
 * <p>
 * Note: The container is configured with reuse set to false, meaning a new container is created for each test run.
 * </p>
 */
@UtilityClass
public class PostgresTestcontainer {

    /**
     * The PostgreSQL Docker container instance.
     */
    private static PostgreSQLContainer<?> container = new PostgreSQLContainer<>(PropertiesUtil.get("db.test.imageVersion"))
            .withReuse(false);

    static {
        System.setProperty("app.environment", "test");
    }

    /**
     * Initializes the test environment by starting the PostgreSQL container, configuring connection properties,
     * and applying Liquibase migrations.
     */
    public void init() {
        container.start();

        String currentSchemaReplace = "currentSchema=%s".formatted(PropertiesUtil.get("db.migrations.defaultSchemaName"));
        String changedUrl = container.getJdbcUrl().replace("loggerLevel=OFF", currentSchemaReplace);
        String envProperty = System.getProperty("app.environment");

        PropertiesUtil.setPropertyValue("db.connection.url.%s".formatted(envProperty), changedUrl);
        PropertiesUtil.setPropertyValue("db.connection.username.%s".formatted(envProperty), container.getUsername());
        PropertiesUtil.setPropertyValue("db.connection.password.%s".formatted(envProperty), container.getPassword());

        ConnectionManager.reloadConfiguration();
        createTables();
    }

    /**
     * Closes the test environment by closing the database connection and stopping the PostgreSQL container.
     */
    public void close() {
        closePool();
        container.stop();
    }

    /**
     * Closes the database connection pool.
     */
    private void closePool() {
        ConnectionManager.closeConnectionPool();
    }

    /**
     * Creates tables by applying Liquibase migrations.
     */
    private void createTables() {
        try (Connection connection = ConnectionManager.getConnection()) {
            LiquibaseMigration.update(PropertiesUtil.get("db.migrations.test.changelog-file"), connection);
        } catch (SQLException | DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
}
