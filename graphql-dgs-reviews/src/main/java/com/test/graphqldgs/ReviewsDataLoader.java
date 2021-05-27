package com.test.graphqldgs;

import com.netflix.graphql.dgs.DgsDataLoader;
import com.test.graphqldgs.domain.Review;
import com.test.graphqldgs.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

@RequiredArgsConstructor
@DgsDataLoader(name = "reviews")
public class ReviewsDataLoader implements MappedBatchLoader<Long, List<Review>> {
    private final ReviewService reviewService;
    private final Executor executor;
    
    @Override
    public CompletionStage<Map<Long, List<Review>>> load(Set<Long> showIds) {
        return CompletableFuture.supplyAsync(() -> reviewService.getReviewsBatch(showIds), executor);
    }
}
