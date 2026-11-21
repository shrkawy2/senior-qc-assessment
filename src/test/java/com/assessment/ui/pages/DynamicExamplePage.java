package com.assessment.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public final class DynamicExamplePage extends BasePage {
    private static final By START = By.cssSelector("#start button");
    private static final By LOADER = By.id("loading");
    private static final By FINISH = By.cssSelector("#finish h4");
    public DynamicExamplePage(WebDriver driver) { super(driver); }

    public DynamicExamplePage start() {
        click(START);
        return this;
    }

    public String awaitResult() {
        // Example 2 inserts a new element into the DOM; locate it after Start.
        // Waiting for the result first avoids treating an absent loader as success.
        String result = visible(FINISH).getText();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(LOADER));
        return result.trim();
    }
}
