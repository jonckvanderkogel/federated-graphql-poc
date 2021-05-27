package com.test.graphqldgs;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
import com.test.graphqldgs.service.ActorService;
import com.test.graphqldgs.service.ShowService;
import com.test.graphqldgs.service.ValidationService;
import graphql.ExecutionResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {DgsAutoConfiguration.class, ShowsDataFetcher.class, ShowService.class, ActorService.class, ValidationService.class})
public class ShowsDataFetcherTest {
    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;
    
    @Test
    public void shouldReturnAllShowsWithoutFilters() {
        String query = """
                {
                    shows {
                        title
                    }
                }
                """;
        
        List<String> titles = dgsQueryExecutor.executeAndExtractJsonPath(query, "data.shows[*].title");
        
        assertThat(titles.size()).isEqualTo(5);
    }
    
    @Test
    public void shouldFilterByTitle() {
        String query = """
                {
                    shows(titleFilter: "T") {
                        title
                    }
                }
                """;
        List<String> titles = dgsQueryExecutor.executeAndExtractJsonPath(query, "data.shows[*].title");
        
        assertThat(titles.size()).isEqualTo(2);
        assertThat(titles).contains("Stranger Things", "The Crown");
    }
    
    @Test
    public void shouldFilterByDirector() {
        String query = """
                {
                    shows(showFilter: {director: "McCarthy"}) {
                        title
                    }
                }
                """;
        List<String> titles = dgsQueryExecutor.executeAndExtractJsonPath(query, "data.shows[*].title");
        
        assertThat(titles.size()).isEqualTo(1);
        assertThat(titles).contains("Orange is the New Black");
    }
    
    @Test
    public void shouldFilterByGenre() {
        String query = """
                {
                    shows(showFilter: {showGenre: DRAMA}) {
                        title
                    }
                }
                """;
        List<String> titles = dgsQueryExecutor.executeAndExtractJsonPath(query, "data.shows[*].title");
        
        assertThat(titles.size()).isEqualTo(3);
        assertThat(titles).contains("The Crown", "Dead to Me", "Orange is the New Black");
    }
    
    @Test
    public void shouldFilterByCombinationOfFilters() {
        String query = """
                {
                    shows(titleFilter: "T", showFilter: {director: "Caron", showGenre: DRAMA}) {
                        title
                    }
                }
                """;
        List<String> titles = dgsQueryExecutor.executeAndExtractJsonPath(query, "data.shows[*].title");
        
        assertThat(titles.size()).isEqualTo(1);
        assertThat(titles).contains("The Crown");
    }
    
    @Test
    public void shouldApplyRatingInput() {
        String mutation = """
                mutation {
                  addRating(rating: {showId: 2, stars: 5}) {
                    	id
                    title
                    averageRating
                  }
                }
                """;
        
        Double rating = dgsQueryExecutor.executeAndExtractJsonPath(mutation, "data.addRating.averageRating");
        assertThat(rating).isEqualTo(5.0);
    }
    
    @Test
    public void shouldGiveErrorIfRatingIsOutsideOfConstraints() {
        String mutation = """
                mutation {
                  addRating(rating: {showId: 2, stars: 6}) {
                    	id
                    title
                    averageRating
                  }
                }
                """;
        
        ExecutionResult result = dgsQueryExecutor.execute(mutation);
        assertThat(result.getErrors()).isNotEmpty();
        assertThat(result.getErrors().get(0).getPath().get(0)).isEqualTo("Invalid field: stars, constraint: must be less than or equal to 5, invalid value: 6");
    }
}
