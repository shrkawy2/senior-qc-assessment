package com.assessment.api.model;

import com.fasterxml.jackson.databind.JsonNode;

public record Book(int id, String title, String description, int pageCount, String excerpt, String publishDate) {
    public static Book from(JsonNode node) {
        return new Book(node.required("id").asInt(), node.required("title").asText(),
                node.required("description").asText(), node.required("pageCount").asInt(),
                node.required("excerpt").asText(), node.required("publishDate").asText());
    }
}
