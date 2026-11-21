package com.assessment.api.tests;

import com.assessment.api.model.Book;
import com.assessment.api.service.BooksService;
import com.assessment.data.TestData;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.lang.reflect.Method;
import java.time.OffsetDateTime;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.blankOrNullString;

public final class BooksApiTests {
    private BooksService books;

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) { books = new BooksService(method.getName()); }

    @DataProvider(name = "existingBooks")
    public Object[][] existingBooks() { return TestData.rows("data/books.json", "existingBooks"); }

    @DataProvider(name = "newBooks")
    public Object[][] newBooks() { return TestData.rows("data/books.json", "newBooks"); }

    @DataProvider(name = "missingBooks")
    public Object[][] missingBooks() { return TestData.rows("data/books.json", "missingBooks"); }

    @Test(groups = "api", dataProvider = "existingBooks", description = "GET an existing book and validate its contract and expected fields")
    public void getsExistingBook(JsonNode expected) {
        Response response = books.getBook(expected.required("id").asInt());
        response.then().log().ifValidationFails().statusCode(200).contentType("application/json")
                .body(matchesJsonSchemaInClasspath("schemas/book.schema.json"))
                .body("id", equalTo(expected.required("id").asInt()))
                .body("title", equalTo(expected.required("title").asText()))
                .body("pageCount", equalTo(expected.required("pageCount").asInt()))
                .body("description", not(blankOrNullString()))
                .body("excerpt", not(blankOrNullString()));
        // Generated publication dates change over time: validate format, not today's exact value.
        OffsetDateTime.parse(response.jsonPath().getString("publishDate"));
    }

    @Test(groups = "api", dataProvider = "newBooks", description = "POST a book and validate every returned field without assuming persistence")
    public void createsBookAndEchoesPayload(JsonNode data) {
        Book book = Book.from(data);
        Response response = books.createBook(book);
        response.then().log().ifValidationFails().statusCode(200).contentType("application/json")
                .body(matchesJsonSchemaInClasspath("schemas/book.schema.json"))
                .body("id", equalTo(book.id()))
                .body("title", equalTo(book.title()))
                .body("description", equalTo(book.description()))
                .body("pageCount", equalTo(book.pageCount()))
                .body("excerpt", equalTo(book.excerpt()));
        Assert.assertEquals(OffsetDateTime.parse(response.jsonPath().getString("publishDate")).toInstant(),
                OffsetDateTime.parse(book.publishDate()).toInstant(), "Publication timestamp must be preserved");
    }

    @Test(groups = "api", dataProvider = "missingBooks", description = "GET a non-existent int32 book ID and validate the 404 problem response")
    public void rejectsMissingBook(JsonNode expected) {
        books.getBook(expected.required("id").asInt()).then().log().ifValidationFails()
                .statusCode(expected.required("status").asInt())
                .contentType("application/problem+json")
                .body("status", equalTo(expected.required("status").asInt()))
                .body("title", equalTo(expected.required("title").asText()))
                .body("type", not(blankOrNullString()))
                .body("traceId", not(blankOrNullString()));
    }
}
