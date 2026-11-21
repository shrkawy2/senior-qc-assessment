package com.assessment.api.service;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

public final class ApiEvidenceFilter implements Filter {
    private final String testName;
    private final AtomicInteger sequence = new AtomicInteger();

    public ApiEvidenceFilter(String testName) { this.testName = testName; }

    @Override
    public Response filter(FilterableRequestSpecification request,
                           FilterableResponseSpecification responseSpec, FilterContext context) {
        Response response = context.next(request, responseSpec);
        String evidence = request.getMethod() + " " + request.getURI() + "\n"
                + "Request body: " + String.valueOf((Object) request.getBody()) + "\n\n"
                + response.getStatusLine() + "\nContent-Type: " + response.getContentType()
                + "\n\n" + response.asPrettyString();
        try {
            Path directory = Path.of("target", "evidence", "api");
            Files.createDirectories(directory);
            Files.writeString(directory.resolve(testName + "-" + sequence.incrementAndGet() + ".txt"),
                    evidence, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot save API evidence", e);
        }
        return response;
    }
}
