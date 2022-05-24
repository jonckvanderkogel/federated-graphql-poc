package com.test.graphql.entityresolver;

import com.apollographql.federation.graphqljava._Entity;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class EntityDataFetcher implements DataFetcher<Object> {
    private final Map<String, EntityResolver<?>> entityResolverMap;

    public EntityDataFetcher(List<EntityResolver<?>> entityResolvers) {
        entityResolverMap = entityResolvers
            .stream()
            .collect(
                Collectors
                    .toMap(EntityResolver::getTypename, r -> r)
            );
    }

    @Override
    public Object get(DataFetchingEnvironment env) {
        log.info(String.format("Argument: %s", env.<List<Map<String, Object>>>getArgument(_Entity.argumentName)));
        return env.<List<Map<String, Object>>>getArgument(_Entity.argumentName)
            .stream()
            .map(reference -> entityResolverMap
                .getOrDefault((String) reference.get("__typename"), defaultEntityResolver)
                .resolveEntity(reference)
            )
            .collect(Collectors.toList());
    }

    private final EntityResolver<Object> defaultEntityResolver = new EntityResolver<>() {

        @Override
        public Object resolveEntity(Map<String, Object> reference) {
            return null;
        }

        @Override
        public String getTypename() {
            return null;
        }
    };
}
