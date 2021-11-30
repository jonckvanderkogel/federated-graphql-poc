package com.test.graphqldgs;

import com.netflix.graphql.dgs.*;
import com.test.graphqldgs.domain.*;
import com.test.graphqldgs.input_argument.RatingInput;
import com.test.graphqldgs.input_argument.ShowFilter;
import com.test.graphqldgs.service.ShowService;
import com.test.graphqldgs.service.ValidationService;
import graphql.execution.DataFetcherResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoader;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.test.utils.DataFetcherUtil;

import static com.test.graphqldgs.service.ValidationService.validate;
import static com.test.utils.DataFetcherUtil.*;

@Slf4j
@DgsComponent
@RequiredArgsConstructor
public class ShowsDataFetcher {
    private final ShowService showService;
    private final ValidationService validationService;

    @DgsData(parentType = "Query", field = "shows")
    public List<Show> shows(
            @InputArgument("titleFilter") String titleFilter, 
            @InputArgument ShowFilter showFilter) {
        return showService
                .getShows()
                .stream()
                .filter(
                        createTitlePredicate(titleFilter)
                                .and(createShowFilterPredicate(showFilter))
                )
                .collect(Collectors.toList());
    }
    
    @DgsData(parentType = "Query", field = "foo")
    public List<Foo> foo() {
        return List.of(new Foo(1L, new Bar(1L)), new Foo(2L, new Bar(2L)));
    }
    
// Normal child datafetcher, this one has the N+1 problem
//    @DgsData(parentType = "Show", field = "actors")
//    public List<Actor> actors(DgsDataFetchingEnvironment dfe) {
//        Show show = dfe.getSource();
//
//        return actorService.getActors(show.getId());
//    }
    
    /*
    Batch child dataloader which doesn't have the N+1 problem
     */
    @DgsData(parentType = "Show", field = "actors")
    public CompletableFuture<List<Actor>> actors(DgsDataFetchingEnvironment dfe) {
        DataLoader<Long, List<Actor>> dataLoader = dfe.getDataLoader(ActorsDataLoader.class);
        Show show = dfe.getSource();
        return dataLoader.load(show.getId());
    }

    @DgsData(parentType = "Mutation", field = "addRating")
    public DataFetcherResult<Show> addRating(@InputArgument RatingInput rating) {
        return validate(rating)
            .map(showService::addRating)
            .fold(
                errorFun(),
                successFun()
            );
    }
    
    private Predicate<Show> createTitlePredicate(String titleFilter) {
        return createPredicate(
                titleFilter,
                s -> t -> s.getTitle().contains(t)
        );
    }

    private Predicate<Show> createShowFilterPredicate(ShowFilter showFilter) {
        return DataFetcherUtil.<Show,ShowGenre>createPredicate(
                extractValue(showFilter, ShowFilter::getShowGenre),
                s -> sg -> s.getGenre().equals(sg)
        )
                .and(
                        createPredicate(
                                extractValue(showFilter, ShowFilter::getDirector),
                                s -> d -> s.getDirector().contains(d)
                        )
                );
    }
}
