package com.assessment.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

public final class TestData {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private TestData() { }

    public static JsonNode read(String resource) {
        try (InputStream input = TestData.class.getClassLoader().getResourceAsStream(resource)) {
            if (input == null) throw new IllegalArgumentException("Missing resource: " + resource);
            return MAPPER.readTree(input);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read test data: " + resource, e);
        }
    }

    public static Object[][] rows(String resource, String section) {
        JsonNode rows = read(resource).required(section);
        if (!rows.isArray() || rows.isEmpty()) throw new IllegalArgumentException("Empty/non-array: " + section);
        Object[][] result = new Object[rows.size()][1];
        for (int i = 0; i < rows.size(); i++) result[i][0] = rows.get(i);
        return result;
    }

    public static Path file(String resource) {
        URL url = TestData.class.getClassLoader().getResource(resource);
        if (url == null) throw new IllegalArgumentException("Missing resource: " + resource);
        try {
            return Path.of(url.toURI()).toAbsolutePath();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid resource URI", e);
        }
    }
}
