package com.test.graphqldgs.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class Show {
    private final Long id;
    @Setter
    private List<Review> reviews;
    
    public Show(String id) {
        this.id = Long.valueOf(id);
    }
}
