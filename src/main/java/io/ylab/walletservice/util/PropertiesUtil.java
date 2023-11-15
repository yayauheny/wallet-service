package io.ylab.walletservice.util;

import io.ylab.walletservice.exception.DatabaseException;
import io.ylab.walletservice.infrastructure.database.ConnectionManager;
import io.ylab.walletservice.infrastructure.database.LiquibaseMigration;
import lombok.experimental.UtilityClass;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utility class for handling application properties.
 * This class provides methods to load properties from the "application.properties" file,
 * replace placeholders in the loaded properties, and retrieve values based on keys.
 */
@UtilityClass
public class PropertiesUtil {

    private final Properties PROPERTIES = new Properties();

    private static final String FILE_NAME = "application.properties";

    static {
        loadProperties();
    }

    /**
     * Loads properties from the "application.properties" file.
     * Handles IOException if the file cannot be read.
     */
    private void loadProperties() {
        try (var inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            System.out.println("Can't read application.properties file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Replaces a placeholder with a specified value in all loaded properties.
     *
     * @param value The value to substitute the placeholder.
     */
    public void setPropertyValue(String key, String value) {
        if (key != null && value != null) {
            PROPERTIES.setProperty(key, value);
            try (OutputStream outputStream = new FileOutputStream(FILE_NAME)) {
                PROPERTIES.store(outputStream, "Updated properties");
            } catch (IOException e) {
                System.out.println("Error saving properties: " + e.getMessage());
            }
        }
    }


    /**
     * Retrieves the value associated with a specified key from the loaded properties.
     *
     * @param key The key for which to retrieve the value.
     * @return The value associated with the key, or null if the key is not found.
     */
    public String get(String key) {
        return PROPERTIES.getProperty(key);
    }

//    static {
//        System.setProperty("app.environment", "dev");
//        ConnectionManager.reloadConfiguration();
//        try (Connection connection = ConnectionManager.getConnection()) {
//            LiquibaseMigration.update(PropertiesUtil.get("db.migrations.changelog-file"), connection);
//        } catch (DatabaseException | SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
