package com.test.graphqldgs.service;

import com.test.graphqldgs.domain.Actor;
import io.vavr.Tuple2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ActorService {
    private final Map<Long, List<Actor>> actors = Map.of(
            1L, List.of(new Actor("Winona Ryder"), new Actor("David Harbour"), new Actor("Finn Wolfhard")),
            2L, List.of(new Actor("Jason Bateman"), new Actor("Laura Finney"), new Actor("Sofia Hublitz")),
            3L, List.of(new Actor("Claire Foy"), new Actor("Olivia Colman"), new Actor("Imelda Staunton")),
            4L, List.of(new Actor("Christina Applegate"), new Actor("Linda Cardellini"), new Actor("Sam McCarthy")),
            5L, List.of(new Actor("Taylor Schilling"), new Actor("Kate Mulgrew"), new Actor("Uzo Aduba"))
    );
    
    public List<Actor> getActors(Long showId) {
        return actors.get(showId);
    }
    
    public Map<Long, List<Actor>> getActorsBatch(Set<Long> showIds) {
        return showIds
                .stream()
                .map(s -> new Tuple2<>(s, actors.get(s)))
                .collect(Collectors.toMap(Tuple2::_1, Tuple2::_2));
    }
}
