package com.test.graphql.entityresolver;

import com.test.graphql.domain.Motorcycle;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MotorcycleEntityResolver implements EntityResolver<Motorcycle> {
    @Override
    public Motorcycle resolveEntity(Map<String, Object> reference) {
        return Motorcycle.generateMotorcycle(reference);
    }

    @Override
    public String getTypename() {
        return "Motorcycle";
    }
}
