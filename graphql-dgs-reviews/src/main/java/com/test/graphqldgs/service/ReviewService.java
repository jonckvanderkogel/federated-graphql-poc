package com.test.graphqldgs.service;

import com.test.graphqldgs.domain.Review;
import io.vavr.Tuple2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReviewService {
    private final Map<Long, List<Review>> actors = Map.of(
            1L, List.of(new Review(1L,"Just awful"), new Review(2L,"Not bad"), new Review(3L, "It was alright")),
            2L, List.of(new Review(4L, "Decent"), new Review(5L,"Seen worse"), new Review(6L, "Best movie ever")),
            3L, List.of(new Review(7L, "Prevent!"), new Review(8L,"Will never get those hours of my life back."), new Review(9L,"Slept through it")),
            4L, List.of(new Review(10L, "Great"), new Review(11L,"Bring out the Oscars"), new Review(12L, "Made my year")),
            5L, List.of(new Review(13L, "Never knew movies could be this bad"), new Review(14L, "I want my money back"), new Review(15L ,"I don't understand why people don't like it, not bad at all."))
    );
    
    public Map<Long, List<Review>> getReviewsBatch(Set<Long> showIds) {
        log.info("Calling batch reviews service");
        return showIds
                .stream()
                .map(s -> new Tuple2<>(s, actors.get(s)))
                .collect(Collectors.toMap(Tuple2::_1, Tuple2::_2));
    }
}
