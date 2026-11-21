package com.assessment.ui;

import com.assessment.config.TestConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import java.util.Locale;

public final class DriverFactory {
    private DriverFactory() { }

    public static WebDriver create() {
        String headless = TestConfig.get("headless");
        if (!headless.equalsIgnoreCase("true") && !headless.equalsIgnoreCase("false")) {
            throw new IllegalArgumentException("headless must be true or false");
        }
        boolean runHeadless = Boolean.parseBoolean(headless);
        String binary = System.getProperty("browser.binary", "");
        return switch (TestConfig.get("browser").toLowerCase(Locale.ROOT)) {
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--window-size=1440,1000");
                if (runHeadless) options.addArguments("--headless=new");
                if (!binary.isBlank()) options.setBinary(binary);
                yield new ChromeDriver(options);
            }
            case "edge" -> {
                EdgeOptions options = new EdgeOptions();
                options.addArguments("--window-size=1440,1000");
                if (runHeadless) options.addArguments("--headless=new");
                if (!binary.isBlank()) options.setBinary(binary);
                yield new EdgeDriver(options);
            }
            default -> throw new IllegalArgumentException("Supported browsers: chrome, edge");
        };
    }
}
