package com.test.graphqldgs;

import com.netflix.graphql.dgs.DgsDataLoader;
import com.test.graphqldgs.domain.Bar;
import com.test.graphqldgs.service.BarService;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

@RequiredArgsConstructor
@DgsDataLoader(name = "bars")
public class BarDataLoader implements MappedBatchLoader<Long, Bar> {
    private final BarService barService;
    private final Executor executor;
    
    @Override
    public CompletionStage<Map<Long, Bar>> load(Set<Long> barIds) {
        return CompletableFuture.supplyAsync(() -> barService.getBarsBatch(barIds), executor);
    }
}
