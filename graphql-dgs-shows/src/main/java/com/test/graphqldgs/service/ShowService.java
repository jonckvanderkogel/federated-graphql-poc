package com.test.graphqldgs.service;

import com.test.graphqldgs.domain.Rating;
import com.test.graphqldgs.domain.Show;
import com.test.graphqldgs.input_argument.RatingInput;
import io.vavr.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.test.graphqldgs.domain.ShowGenre.*;

@Service
public class ShowService {
    private final Lazy<List<Show>> shows = Lazy.of(
            () -> List.of(
                    new Show(1L, "Stranger Things", "Matt Duffer", 2016, SCIFI),
                    new Show(2L, "Ozark", "Jason Bateman", 2017, THRILLER),
                    new Show(3L, "The Crown", "Benjamin Caron", 2016, DRAMA),
                    new Show(4L, "Dead to Me", "Kat Coiro", 2019, DRAMA),
                    new Show(5L, "Orange is the New Black", "Andrew McCarthy", 2013, DRAMA)
            )
    );
    
    private static final Show EMPTY_SHOW = new Show(-1L, "No Title", "No Director", 1, DRAMA); 

    public List<Show> getShows() {
        return shows.get();
    }
    
    public Show getShowById(Long id) {
        return shows
                .get()
                .stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElseGet(() -> EMPTY_SHOW);
    }
    
    public Show addRating(RatingInput ratingInput) {
        Show show = getShowById(ratingInput.getShowId());
        show
                .getRatings()
                .add(new Rating(ratingInput.getStars()));
        
        return show;
    }
}
