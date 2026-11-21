package com.assessment.api.service;

import com.assessment.api.model.Book;
import com.assessment.config.TestConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.time.Duration;

public final class BooksService {
    private static final String BOOKS = "/api/v1/Books";
    private static final ObjectMapper JSON = new ObjectMapper();
    private final RequestSpecification specification;

    public BooksService(String testName) {
        int timeout = TestConfig.positiveInt("api.timeout.millis");
        specification = new RequestSpecBuilder()
                .setBaseUri(TestConfig.get("api.baseUrl"))
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .setConfig(RestAssuredConfig.config().httpClient(HttpClientConfig.httpClientConfig()
                        .connectTimeout(Duration.ofMillis(timeout))
                        .readTimeout(Duration.ofMillis(timeout))))
                .addFilter(new ApiEvidenceFilter(testName))
                .build();
    }

    public Response getBook(int id) {
        return RestAssured.given().spec(specification).pathParam("id", id).when().get(BOOKS + "/{id}");
    }

    public Response createBook(Book book) {
        try {
            return RestAssured.given().spec(specification).body(JSON.writeValueAsString(book)).when().post(BOOKS);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot serialize book", e);
        }
    }
}
