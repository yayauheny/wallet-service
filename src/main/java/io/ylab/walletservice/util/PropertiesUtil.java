package io.ylab.walletservice.util;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.Properties;

@UtilityClass
public class PropertiesUtil {

    private final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private void loadProperties() {
        try (var inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            System.out.println("Can't read application.properties file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void replacePlaceholder(String placeholder, String value) {
        if (value != null) {
            for (String key : PROPERTIES.stringPropertyNames()) {
                String replacedValue = PROPERTIES.getProperty(key).replace("${" + placeholder + "}", value);
                PROPERTIES.setProperty(key, replacedValue);
            }
        }
    }

    public String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
