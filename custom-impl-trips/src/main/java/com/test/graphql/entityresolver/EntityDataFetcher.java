package com.test.graphql.entityresolver;

import com.apollographql.federation.graphqljava._Entity;
import com.test.graphql.domain.Motorcycle;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
