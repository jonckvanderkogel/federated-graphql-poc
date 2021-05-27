package com.test.graphqldgs;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsEntityFetcher;
import com.test.graphqldgs.domain.Review;
import com.test.graphqldgs.domain.Show;
import com.test.graphqldgs.domain.Bar;
import com.test.graphqldgs.service.BarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoader;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@DgsComponent
@RequiredArgsConstructor
public class DataFetcher {
    private final BarService barService;
    
    @DgsEntityFetcher(name = "Show")
    public Show shows(Map<String, Object> values) {
        return new Show(Long.valueOf((String)values.get("id")));
    }
    
    @DgsData(parentType = "Show", field = "reviews")
    public CompletableFuture<List<Review>> reviews(DgsDataFetchingEnvironment dfe) {
        DataLoader<Long, List<Review>> dataLoader = dfe.getDataLoader(ReviewsDataLoader.class);
        Show show = dfe.getSource();
        return dataLoader.load(show.getId());
    }
    
    @DgsData(parentType = "Query", field = "bars")
    public List<Bar> bars() {
        return barService.getAllBars();
    }
    
    @DgsEntityFetcher(name = "Bar")
    public CompletableFuture<Bar> barEntityFetcher(Map<String, Object> values, DgsDataFetchingEnvironment dfe) {
        DataLoader<Long, Bar> dataLoader = dfe.getDataLoader(BarDataLoader.class);
        
        return dataLoader.load(Long.valueOf((String)values.get("id")));
    }
}
