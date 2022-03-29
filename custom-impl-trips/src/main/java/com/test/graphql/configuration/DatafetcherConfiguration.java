package com.test.graphql.configuration;

import com.test.graphql.datafetcher.TripsDataLoader;
import com.test.graphql.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Supplier;

@Configuration
@RequiredArgsConstructor
public class DatafetcherConfiguration {
    private final TripService tripService;

    @Bean
    public Supplier<TripsDataLoader> tripsDataLoaderSupplier() {
        return () -> createTripsDataLoader(tripService);
    }

    private TripsDataLoader createTripsDataLoader(TripService tripService) {
        return new TripsDataLoader(tripService);
    }
}
