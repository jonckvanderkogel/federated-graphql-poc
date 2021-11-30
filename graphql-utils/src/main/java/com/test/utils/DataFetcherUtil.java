package com.test.utils;

import graphql.GraphQLError;
import graphql.execution.DataFetcherResult;
import io.vavr.control.Either;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class DataFetcherUtil {
    public static <T> DataFetcherResult<T> createDataFetcherResult(Either<GraphQLError, T> either) {
        if (either.isRight()) {
            return DataFetcherResult.<T>newResult()
                    .data(either.get())
                    .build();
        } else {
            return DataFetcherResult.<T>newResult()
                    .error(either.getLeft())
                    .build();
        }
    }

    public static <T> Function<T, DataFetcherResult<T>> successFun() {
        return r -> DataFetcherResult.<T>newResult().data(r).build();
    }

    public static <T> Function<GraphQLError, DataFetcherResult<T>> errorFun() {
        return l -> DataFetcherResult.<T>newResult().error(l).build();
    }

    public static <T,U> Predicate<T> createPredicate(U arg, Function<T, Function<U, Boolean>> fun) {
        return (s) -> Optional.ofNullable(arg)
                .map(fun.apply(s))
                .orElseGet(() -> true);
    }

    public static <T, U> U extractValue(T arg, Function<T, U> fun) {
        return Optional
                .ofNullable(arg)
                .map(fun)
                .orElseGet(() -> null);
    }
}
