package com.test.graphql.domain;

import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@Getter
public enum Brand {
    APRILLA("Aprilla"),
    BMW("BMW"),
    DUCATI("Ducati"),
    HARLEY_DAVIDSON("Harley Davidson"),
    HONDA("Honda"),
    INDIAN("Indian Motorcycle"),
    KAWASAKI("Kawasaki"),
    KTM("KTM"),
    ROYAL_ENFIELD("Royal Enfield"),
    SUZUKI("Suzuki"),
    TRIUMPH("Triumph"),
    YAMAHA("Yamaha"),
    BRAND_UNKNOWN("Brand unknown");

    private final String brandName;

    Brand(String brandName) {
        this.brandName = brandName;
    }

    private static final Map<String, Brand> stringToEnum = Stream.of(values())
        .collect(toMap(Object::toString, Function.identity()));

    public static Brand fromString(final String name) {
        return stringToEnum.getOrDefault(name, BRAND_UNKNOWN);
    }
}
