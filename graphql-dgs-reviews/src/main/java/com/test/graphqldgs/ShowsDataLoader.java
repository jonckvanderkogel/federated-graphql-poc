package com.test.graphqldgs;

import com.netflix.graphql.dgs.DgsDataLoader;
import com.test.graphqldgs.domain.Show;
import io.vavr.Tuple2;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@DgsDataLoader(name = "shows")
public class ShowsDataLoader implements MappedBatchLoader<Long, Show> {
    private final Executor executor;
    
    @Override
    public CompletionStage<Map<Long, Show>> load(Set<Long> showIds) {
        return CompletableFuture.supplyAsync(() -> showIds
                .stream()
                .map(id -> new Tuple2<>(id, new Show(id)))
                .collect(Collectors.toMap(Tuple2::_1, Tuple2::_2))
                , executor
        );
    }
}
