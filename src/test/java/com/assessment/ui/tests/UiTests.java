package com.assessment.ui.tests;

import com.assessment.data.TestData;
import com.assessment.ui.pages.HomePage;
import com.assessment.ui.pages.UploadResultPage;
import com.fasterxml.jackson.databind.JsonNode;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.nio.file.Path;

public final class UiTests extends BaseUiTest {
    @Test(groups = "ui", description = "Upload a small image from the home page and verify success and filename")
    public void uploadsSmallImage() {
        JsonNode data = TestData.read("data/ui.json").required("upload");
        Path image = TestData.file(data.required("resource").asText());
        UploadResultPage result = new HomePage(driver).open()
                .openFileUpload().selectFile(image).submit();

        Assert.assertEquals(result.heading(), data.required("successHeading").asText(), "Upload success heading");
        Assert.assertEquals(result.uploadedFilename(), image.getFileName().toString(), "Exact uploaded filename");
    }

    @Test(groups = "ui", description = "Open Dynamic Loading Example 2, start, wait for result and verify text")
    public void displaysDynamicallyLoadedText() {
        String expected = TestData.read("data/ui.json").required("dynamicLoading").required("expectedText").asText();
        String actual = new HomePage(driver).open()
                .openDynamicLoading().openExampleTwo().start().awaitResult();

        Assert.assertEquals(actual, expected, "Dynamically inserted result text");
    }
}
