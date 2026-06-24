package com.hospital.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection {
    private static final String CONFIG_FILE = "application.properties";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        Properties properties = loadProperties();

        String url = readConfig(properties, "spring.datasource.url", "DB_URL");
        String username = readConfig(properties, "spring.datasource.username", "DB_USERNAME");
        String password = readConfig(properties, "spring.datasource.password", "DB_PASSWORD");

        return DriverManager.getConnection(url, username, password);
    }

    public static boolean testConnection() {
        try (Connection connection = getConnection()) {
            return connection.isValid(2);
        } catch (SQLException exception) {
            System.err.println("Database connection failed: " + exception.getMessage());
            return false;
        }
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();

        try (InputStream inputStream = DatabaseConnection.class
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {
            if (inputStream == null) {
                throw new IllegalStateException(CONFIG_FILE + " not found in src/main/resources");
            }
            properties.load(inputStream);
            return properties;
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load database configuration", exception);
        }
    }

    private static String readConfig(Properties properties, String propertyName, String environmentName) {
        String environmentValue = System.getenv(environmentName);
        if (environmentValue != null && !environmentValue.isBlank()) {
            return environmentValue;
        }

        String propertyValue = properties.getProperty(propertyName);
        if (propertyValue == null || propertyValue.isBlank()) {
            throw new IllegalStateException(propertyName + " is missing from " + CONFIG_FILE);
        }

        return propertyValue.trim();
    }
}
