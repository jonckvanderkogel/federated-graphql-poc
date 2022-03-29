package com.test.graphql.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class Motorcycle {
    private final long id;
    @Setter
    private List<Trip> trips;

    public static Motorcycle generateMotorcycle(Map<String, Object> reference) {
        if (!(reference.get("id") instanceof final String id)) {
            return null;
        }

        return new Motorcycle(Long.parseLong(id));
    }
}
