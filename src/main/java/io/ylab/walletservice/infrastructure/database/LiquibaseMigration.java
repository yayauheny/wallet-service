package io.ylab.walletservice.infrastructure.database;

import io.ylab.walletservice.exception.DatabaseException;
import io.ylab.walletservice.util.PropertiesUtil;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Utility class for managing Liquibase migrations in the application.
 *
 * This class provides a method to update the database schema using Liquibase based on a specified changelog file.
 * Additionally, it allows the creation of a schema before executing the Liquibase update operation.
 *
 */
@UtilityClass
public class LiquibaseMigration {

    /**
     * Updates the database schema using Liquibase based on the specified changelog file.
     * Optionally creates the Liquibase schema before executing the update.
     *
     * @throws RuntimeException if an error occurs during the update operation.
     */
    public void update() {
        try {
            String changelogFile = PropertiesUtil.get("db.migrations.changelog-file");
            String liquibaseSchemaName = PropertiesUtil.get("db.migrations.liquibaseSchemaName");

            createSchema(liquibaseSchemaName);

            Connection connection = ConnectionManager.getConnection();
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setLiquibaseSchemaName(liquibaseSchemaName);
            Liquibase liquibase = new Liquibase(changelogFile, new ClassLoaderResourceAccessor(), database);

            liquibase.update();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a database schema if it does not exist.
     *
     * @param schemaName The name of the schema to be created.
     * @throws RuntimeException if an error occurs during the schema creation.
     */
    private void createSchema(String schemaName) {
        String query = "CREATE SCHEMA IF NOT EXISTS %s;".formatted(schemaName);
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.execute();
        } catch (SQLException | DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
}
