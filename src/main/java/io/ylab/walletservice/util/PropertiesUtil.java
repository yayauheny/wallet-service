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

    public String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
