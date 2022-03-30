package com.test.graphql.datafetcher;

import com.test.graphql.DataFetcherWrapper;
import com.test.graphql.domain.Brand;
import com.test.graphql.domain.Motorcycle;
import com.test.graphql.domain.Trip;
import io.vavr.Tuple2;
import org.dataloader.DataLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Configuration
public class MotorcycleDataFetchers {

    @Bean
    public DataFetcherWrapper<CompletableFuture<List<Trip>>> tripsFetcher() {
        return new DataFetcherWrapper<>(
            "Motorcycle",
            "trips",
            dataFetchingEnvironment -> {
                DataLoader<Tuple2<Long, Brand>, List<Trip>> tripsDataLoader = dataFetchingEnvironment.getDataLoader("trips");
                Motorcycle motorcycle = dataFetchingEnvironment.getSource();
                return tripsDataLoader.load(new Tuple2<>(motorcycle.getId(), motorcycle.getBrand()));
            }
        );
    }
}
