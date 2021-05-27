package com.test.graphqldgs.service;

import com.github.javafaker.Faker;
import com.test.graphqldgs.domain.Bar;
import io.vavr.Tuple2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Slf4j
@Service
public class BarService {
    private final Faker faker = new Faker();
    
    private final Map<Long, Bar> bars = LongStream
            .rangeClosed(1, 5)
            .mapToObj(id -> new Tuple2<>(id, new Bar(id, faker.beer().name())))
            .collect(Collectors.toMap(Tuple2::_1, Tuple2::_2));
    
    public List<Bar> getAllBars() {
        return List.copyOf(bars.values());
    }
    
    public Bar getBar(Long id) {
        return bars.get(id);
    }
    
    public Map<Long, Bar> getBarsBatch(Set<Long> ids) {
        log.info("Calling bars batch");
        return ids
                .stream()
                .map(s -> new Tuple2<>(s, bars.get(s)))
                .collect(Collectors.toMap(Tuple2::_1, Tuple2::_2));
    }
}
