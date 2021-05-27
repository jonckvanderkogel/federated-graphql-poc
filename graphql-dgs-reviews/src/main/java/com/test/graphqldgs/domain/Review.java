package com.test.graphqldgs.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Review {
    private final Long id;
    private final String description;
}
