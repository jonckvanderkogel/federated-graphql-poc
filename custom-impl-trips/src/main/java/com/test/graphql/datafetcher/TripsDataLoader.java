package com.test.graphql.datafetcher;

import com.test.graphql.domain.Trip;
import com.test.graphql.service.TripService;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RequiredArgsConstructor
public class TripsDataLoader implements MappedBatchLoader<Long, List<Trip>> {
    private final TripService tripService;

    @Override
    public CompletionStage<Map<Long, List<Trip>>> load(Set<Long> motorcycleIds) {
        return CompletableFuture.supplyAsync(() -> tripService.getTripsBatch(motorcycleIds));
    }
}
