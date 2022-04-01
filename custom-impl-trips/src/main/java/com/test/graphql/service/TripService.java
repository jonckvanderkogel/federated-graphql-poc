package com.test.graphql.service;

import com.github.javafaker.Faker;
import com.test.graphql.domain.Brand;
import com.test.graphql.domain.Person;
import com.test.graphql.domain.Trip;
import io.vavr.Tuple2;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TripService {
    private static final Faker FAKER = new Faker();
    private final Map<Long, List<Trip>> database = new HashMap<>();
    private final Map<Brand, List<Person>> brandFansDatabase = new HashMap<>();
    private static SplittableRandom RANDOM = new SplittableRandom();

    public List<Trip> getTrips(Tuple2<Long, Brand> reference) {
        return database
            .computeIfAbsent(reference._1(), i -> IntStream
                .range(1, RANDOM.nextInt(2,6))
                .mapToObj(ignored -> new Trip(FAKER.address().cityName(), FAKER.address().cityName(), getBrandFans(reference._2())))
                .collect(Collectors.toList())
            );
    }

    private List<Person> getBrandFans(Brand brand) {
        return brandFansDatabase
            .computeIfAbsent(brand, i -> IntStream
                .range(1, RANDOM.nextInt(2, 10))
                .mapToObj(ignored -> new Person(FAKER.funnyName().name()))
                .collect(Collectors.toList())
            );
    }

    public Map<Tuple2<Long, Brand>, List<Trip>> getTripsBatch(Set<Tuple2<Long, Brand>> references) {
        return references
            .stream()
            .map(s -> new Tuple2<>(s, getTrips(s)))
            .collect(Collectors.toMap(Tuple2::_1, Tuple2::_2));
    }
}
