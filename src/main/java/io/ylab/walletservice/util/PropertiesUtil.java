package io.ylab.walletservice.util;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.Properties;

/**
 * Utility class for handling application properties.
 * This class provides methods to load properties from the "application.properties" file,
 * replace placeholders in the loaded properties, and retrieve values based on keys.
 *
 */
@UtilityClass
public class PropertiesUtil {

    private final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    /**
     * Loads properties from the "application.properties" file.
     * Handles IOException if the file cannot be read.
     */
    private void loadProperties() {
        try (var inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            System.out.println("Can't read application.properties file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Replaces a placeholder with a specified value in all loaded properties.
     *
     * @param placeholder The placeholder to replace.
     * @param value       The value to substitute the placeholder.
     */
    public void setPropertyValue(String key, String value) {
        if (key != null && value != null) {
            PROPERTIES.setProperty(key, value);
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
}
