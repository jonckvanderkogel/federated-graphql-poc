package com.test.graphql.entityresolver;

import java.util.Map;

public interface EntityResolver<T> {
    T resolveEntity(Map<String, Object> reference);

    String getTypename();
}
