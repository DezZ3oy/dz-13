package org.example;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConfig {
    private static final Properties properties = new Properties();
    private static final Logger logger = Logger.getLogger(DatabaseConfig.class.getName());
    private static final String PROPERTIES_FILE = "db.properties";

    static {
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                logger.log(Level.SEVERE, "Sorry, unable to find {0}", PROPERTIES_FILE);
            } else {
                properties.load(input);
                logger.log(Level.INFO, "Database properties loaded successfully from {0}", PROPERTIES_FILE);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading database properties", e);
        }
    }

    public static String getUrl() {
        return properties.getProperty("db.url", "jdbc:mysql://localhost:3306/defaultdb");
    }

    public static String getUsername() {
        return properties.getProperty("db.username", "root");
    }

    public static String getPassword() {
        return properties.getProperty("db.password", "");
    }
}
