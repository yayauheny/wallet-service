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

@UtilityClass
public class LiquibaseMigration {

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
            e.printStackTrace();
        }
    }

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
