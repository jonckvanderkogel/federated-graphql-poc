package com.test.graphqldgs.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class Show {
    private final Long id;
    private final String title;
    private final String director;
    private final Integer releaseYear;
    private final ShowGenre genre;
    @Setter
    private List<Actor> actors;
    
    @JsonIgnore
    private final List<Rating> ratings = new ArrayList<>();
    
    public Double getAverageRating() {
        return ratings
                .stream()
                .collect(Collectors.averagingInt(Rating::getStars));
    }
}
