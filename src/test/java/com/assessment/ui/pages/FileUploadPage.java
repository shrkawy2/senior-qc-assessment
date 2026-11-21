package com.assessment.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileUploadPage extends BasePage {
    private static final By FILE_INPUT = By.id("file-upload");
    private static final By SUBMIT = By.id("file-submit");

    public FileUploadPage(WebDriver driver) { super(driver); }

    public FileUploadPage selectFile(Path image) {
        if (!Files.isRegularFile(image)) throw new IllegalArgumentException("File not found: " + image);
        visible(FILE_INPUT).sendKeys(image.toAbsolutePath().toString());
        return this;
    }

    public UploadResultPage submit() {
        click(SUBMIT);
        return new UploadResultPage(driver);
    }
}
