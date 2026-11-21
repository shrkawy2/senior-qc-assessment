package com.assessment.ui.pages;

import com.assessment.config.TestConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public final class HomePage extends BasePage {
    private static final By FILE_UPLOAD = By.linkText("File Upload");
    private static final By DYNAMIC_LOADING = By.linkText("Dynamic Loading");

    public HomePage(WebDriver driver) { super(driver); }

    public HomePage open() {
        driver.get(TestConfig.get("ui.baseUrl"));
        visible(FILE_UPLOAD);
        return this;
    }

    public FileUploadPage openFileUpload() {
        click(FILE_UPLOAD);
        return new FileUploadPage(driver);
    }

    public DynamicLoadingPage openDynamicLoading() {
        click(DYNAMIC_LOADING);
        return new DynamicLoadingPage(driver);
    }
}
