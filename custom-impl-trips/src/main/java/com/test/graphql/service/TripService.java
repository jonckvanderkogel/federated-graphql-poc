package com.test.graphql.service;

import com.github.javafaker.Faker;
import com.test.graphql.domain.Trip;
import io.vavr.Tuple2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class TripService {
    private static final Faker FAKER = new Faker();
    private final Map<Long, List<Trip>> database = new HashMap<>();
    private static SplittableRandom RANDOM = new SplittableRandom();

    public List<Trip> getTrips(Long motorcycleId) {
        return database
            .computeIfAbsent(motorcycleId, i -> IntStream
                .range(1, RANDOM.nextInt(2,6))
                .mapToObj(ignored -> new Trip(FAKER.address().cityName(), FAKER.address().cityName()))
                .collect(Collectors.toList())
            );
    }

    public Map<Long, List<Trip>> getTripsBatch(Set<Long> motorcycleIds) {
        log.info("Calling batch trip service");
        return motorcycleIds
            .stream()
            .map(s -> new Tuple2<>(s, getTrips(s)))
            .collect(Collectors.toMap(Tuple2::_1, Tuple2::_2));
    }
}
