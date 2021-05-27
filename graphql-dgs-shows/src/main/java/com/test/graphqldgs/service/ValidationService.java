package com.test.graphqldgs.service;

import com.netflix.graphql.types.errors.ErrorType;
import graphql.GraphQLError;
import io.vavr.control.Either;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.function.Function;

import static com.netflix.graphql.types.errors.TypedGraphQLError.newBuilder;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Service
public class ValidationService {
    private final static Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    
    public <T, U> Either<GraphQLError, U> performValidatedCall(T arg, Function<T, U> fun) {
        var validationResult = VALIDATOR.validate(arg);
        return validationResult.isEmpty() ? Either.right(fun.apply(arg)) : Either.left(
                    newBuilder()
                            .errorType(ErrorType.BAD_REQUEST)
                            .message(validationResult
                                    .stream()
                                    .map(ConstraintViolation::getMessage)
                                    .collect(joining(" "))
                            )
                            .path(validationResult
                                    .stream()
                                    .map(r -> String.format("Invalid field: %s, constraint: %s, invalid value: %s", r.getPropertyPath().toString(), r.getMessage(), r.getInvalidValue()))
                                    .collect(toList())
                            )
                            .build()
            );
    }
}
