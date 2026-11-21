package com.assessment.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public final class UploadResultPage extends BasePage {
    private static final By HEADING = By.cssSelector(".example h3");
    private static final By UPLOADED_FILE = By.id("uploaded-files");

    public UploadResultPage(WebDriver driver) { super(driver); }
    public String heading() { return visible(HEADING).getText().trim(); }
    public String uploadedFilename() { return visible(UPLOADED_FILE).getText().trim(); }
}
