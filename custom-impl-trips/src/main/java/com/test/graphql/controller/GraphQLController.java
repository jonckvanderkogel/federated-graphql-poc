package com.test.graphql.controller;

import com.test.graphql.datafetcher.TripsDataLoader;
import com.test.graphql.domain.Trip;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderFactory;
import org.dataloader.DataLoaderRegistry;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GraphQLController {
    private final GraphQL graphQL;
    private final Supplier<TripsDataLoader> tripsDataLoaderSupplier;
    public static final String FEDERATED_TRACING_HEADER_NAME = "apollo-federation-include-trace";

    @PostMapping(path = "/graphql")
    public Mono<Map<String, Object>> graphql(@RequestBody GraphQLRequest graphQLRequest, ServerHttpRequest request) {
        Map<Object, Object> contextMap = new HashMap<>();
        String federatedTracingHeaderValue = request.getHeaders().getFirst(FEDERATED_TRACING_HEADER_NAME);
        if (federatedTracingHeaderValue != null) {
            contextMap.put(FEDERATED_TRACING_HEADER_NAME, federatedTracingHeaderValue);
        }
        log.info(String.format("Context: %s", contextMap));
        DataLoader<Long, List<Trip>> tripsDataLoader = DataLoaderFactory.newMappedDataLoader(tripsDataLoaderSupplier.get());
        DataLoaderRegistry registry = new DataLoaderRegistry();
        registry.register("trips", tripsDataLoader);

        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
            .graphQLContext(contextMap)
            .query(graphQLRequest.getQuery())
            .dataLoaderRegistry(registry)
            .variables(graphQLRequest.getVariables())
            .operationName(graphQLRequest.getOperationName())
            .build();

        ExecutionResult result = graphQL.execute(executionInput);

        return Mono.just(result.toSpecification());
    }
}
