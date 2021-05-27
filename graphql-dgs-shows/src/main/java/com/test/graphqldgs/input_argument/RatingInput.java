package com.test.graphqldgs.input_argument;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class RatingInput {
    @NotNull
    private Long showId;
    @Min(1)
    @Max(5)
    private Integer stars;
}
