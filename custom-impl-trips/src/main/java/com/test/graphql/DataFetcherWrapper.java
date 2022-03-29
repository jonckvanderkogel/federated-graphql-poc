package com.test.graphql;

import graphql.schema.DataFetcher;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DataFetcherWrapper<T> {
    private final String parentType;
    private final String fieldName;
    private final DataFetcher<T> dataFetcher;
}
