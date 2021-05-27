package com.test.graphqldgs.input_argument;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.test.graphqldgs.domain.ShowGenre;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ShowFilter {
    private String director;
    private ShowGenre showGenre;
}
