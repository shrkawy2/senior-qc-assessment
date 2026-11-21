package com.assessment.config;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

public final class TestConfig {
    private static final Properties DEFAULTS = load();

    private TestConfig() { }

    private static Properties load() {
        Properties properties = new Properties();
        try (InputStream stream = TestConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (stream == null) throw new IllegalStateException("Missing config.properties");
            properties.load(stream);
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read configuration", e);
        }
    }

    public static String get(String key) {
        String value = System.getProperty(key, DEFAULTS.getProperty(key));
        if (value == null || value.isBlank()) throw new IllegalArgumentException("Missing config: " + key);
        return value.trim();
    }

    public static int positiveInt(String key) {
        int value = Integer.parseInt(get(key));
        if (value <= 0) throw new IllegalArgumentException(key + " must be positive");
        return value;
    }

    public static Duration waitTimeout() {
        return Duration.ofSeconds(positiveInt("wait.seconds"));
    }
}
