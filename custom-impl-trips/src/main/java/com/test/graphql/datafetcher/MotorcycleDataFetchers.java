package com.test.graphql.datafetcher;

import com.test.graphql.DataFetcherWrapper;
import com.test.graphql.domain.Motorcycle;
import com.test.graphql.domain.Trip;
import com.test.graphql.service.TripService;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Configuration
public class MotorcycleDataFetchers {
    private final TripService tripService;

    @Bean
    public DataFetcherWrapper<CompletableFuture<List<Trip>>> tripsFetcher() {
        return new DataFetcherWrapper<>(
            "Motorcycle",
            "trips",
            dataFetchingEnvironment -> {
                DataLoader<Long, List<Trip>> tripsDataLoader = dataFetchingEnvironment.getDataLoader("trips");
                Motorcycle motorcycle = dataFetchingEnvironment.getSource();
                return tripsDataLoader.load(motorcycle.getId());
            }
        );
    }
}
