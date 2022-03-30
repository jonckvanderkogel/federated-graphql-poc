package com.test.graphql.domain;

import java.util.List;

public record Trip(String from, String to, List<Person> companions) {
}
