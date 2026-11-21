package com.assessment.ui.tests;

import com.assessment.config.TestConfig;
import com.assessment.ui.DriverFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

public abstract class BaseUiTest {
    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void startBrowser() {
        driver = DriverFactory.create();
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(TestConfig.positiveInt("pageLoad.seconds")));
    }

    @AfterMethod(alwaysRun = true)
    public void captureEvidenceAndClose(ITestResult result) throws Exception {
        if (driver == null) return;
        try {
            Path directory = Path.of("target", "evidence", "ui");
            Files.createDirectories(directory);
            String name = result.getMethod().getMethodName() + "-" + (result.isSuccess() ? "PASS" : "FAIL");
            Files.write(directory.resolve(name + ".png"), ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
            Files.writeString(directory.resolve(name + ".txt"),
                    "URL: " + driver.getCurrentUrl() + System.lineSeparator()
                    + "Browser: " + ((RemoteWebDriver) driver).getCapabilities().getBrowserName()
                    + " " + ((RemoteWebDriver) driver).getCapabilities().getBrowserVersion(), StandardCharsets.UTF_8);
            Reporter.log("UI evidence: target/evidence/ui/" + name + ".png", true);
        } catch (Exception evidenceError) {
            // Preserve the original test failure; do not replace it with a screenshot error.
            if (result.isSuccess()) throw evidenceError;
            Reporter.log("Evidence capture failed: " + evidenceError.getMessage(), true);
        } finally {
            driver.quit();
            driver = null;
        }
    }
}
