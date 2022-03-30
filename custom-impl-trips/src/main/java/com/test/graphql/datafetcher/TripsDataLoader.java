package com.test.graphql.datafetcher;

import com.test.graphql.domain.Brand;
import com.test.graphql.domain.Trip;
import com.test.graphql.service.TripService;
import io.vavr.Tuple2;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RequiredArgsConstructor
public class TripsDataLoader implements MappedBatchLoader<Tuple2<Long, Brand>, List<Trip>> {
    private final TripService tripService;

    @Override
    public CompletionStage<Map<Tuple2<Long, Brand>, List<Trip>>> load(Set<Tuple2<Long, Brand>> references) {
        return CompletableFuture.supplyAsync(() -> tripService.getTripsBatch(references));
    }
}
