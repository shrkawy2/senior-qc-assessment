package com.assessment.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public final class DynamicLoadingPage extends BasePage {
    private static final By EXAMPLE_TWO = By.cssSelector("a[href='/dynamic_loading/2']");
    public DynamicLoadingPage(WebDriver driver) { super(driver); }

    public DynamicExamplePage openExampleTwo() {
        click(EXAMPLE_TWO);
        return new DynamicExamplePage(driver);
    }
}
