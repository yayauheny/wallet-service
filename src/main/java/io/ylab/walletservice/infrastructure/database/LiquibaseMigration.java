package io.ylab.walletservice.infrastructure.database;

import io.ylab.walletservice.util.PropertiesUtil;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;

@UtilityClass
public class LiquibaseMigration {

    public void update() {
        try {
            Connection connection = ConnectionManager.getConnection();
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            String changelogFile = PropertiesUtil.get("changelog-file");
            Liquibase liquibase = new Liquibase(changelogFile, new ClassLoaderResourceAccessor(), database);

            liquibase.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
