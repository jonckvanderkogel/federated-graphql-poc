package com.test.graphqldgs;

import com.netflix.graphql.dgs.DgsDataLoader;
import com.test.graphqldgs.domain.Actor;
import com.test.graphqldgs.service.ActorService;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

@RequiredArgsConstructor
@DgsDataLoader(name = "actors")
public class ActorsDataLoader implements MappedBatchLoader<Long, List<Actor>> {
    private final ActorService actorService;
    private final Executor executor;
    
    @Override
    public CompletionStage<Map<Long, List<Actor>>> load(Set<Long> showIds) {
        return CompletableFuture.supplyAsync(() -> actorService.getActorsBatch(showIds), executor);
    }
}
