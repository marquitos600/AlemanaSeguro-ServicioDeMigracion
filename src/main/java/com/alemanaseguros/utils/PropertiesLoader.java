package com.alemanaseguros.utils;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
    public static Properties loadProperties() throws Exception {
        Properties properties = new Properties();
        try (InputStream input = PropertiesLoader.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new Exception("Unable to find application.properties");
            }
            properties.load(input);
        }
        return properties;
    }
}
