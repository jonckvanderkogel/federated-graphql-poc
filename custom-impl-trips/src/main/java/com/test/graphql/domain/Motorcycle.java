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
    private final Brand brand;
    @Setter
    private List<Trip> trips;

    public static Motorcycle generateMotorcycle(Map<String, Object> reference) {
        if (!(reference.get("id") instanceof final String id) || !(reference.get("brand") instanceof final String brand)) {
            return null;
        }

        return new Motorcycle(Long.parseLong(id), Brand.fromString(brand));
    }
}
