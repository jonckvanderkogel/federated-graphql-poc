package com.test.graphql.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class GraphQLRequest {
    private final String query;
    private final String operationName;
    private final Map<String, Object> variables;

    @JsonCreator
    public GraphQLRequest(@JsonProperty("query") String query,
                          @JsonProperty("operationName") String operationName,
                          @JsonProperty("variables") Map<String, Object> variables) {
        this.query = query;
        this.operationName = operationName;
        this.variables = variables != null ? variables : Collections.emptyMap();
    }
}
