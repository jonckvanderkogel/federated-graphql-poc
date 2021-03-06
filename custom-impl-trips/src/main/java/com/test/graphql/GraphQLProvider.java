package com.test.graphql;

import com.apollographql.federation.graphqljava.Federation;
import com.apollographql.federation.graphqljava._Entity;
import com.apollographql.federation.graphqljava.tracing.FederatedTracingInstrumentation;
import com.test.graphql.domain.Motorcycle;
import com.test.graphql.entityresolver.EntityDataFetcher;
import com.test.tailrecursion.TailCall;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.test.tailrecursion.TailCalls.done;
import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class GraphQLProvider {
    private final EntityDataFetcher entityDataFetcher;

    @Bean
    public GraphQL graphQL(GraphQLSchema schema) {
        return GraphQL
            .newGraphQL(schema)
            .instrumentation(new FederatedTracingInstrumentation())
            .build();
    }

    @Bean
    public GraphQLSchema buildSchema(RuntimeWiring runtimeWiring) throws IOException {
        ClassPathResource resource = new ClassPathResource("schema.graphqls");
        String sdl = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);

        return Federation.transform(typeRegistry, runtimeWiring)
            .fetchEntities(entityDataFetcher)
            .resolveEntityType(
                env -> {
                    final String src = env.getObject().getClass().getSimpleName();

                    if (env.getSchema().containsType(src)) {
                        return env.getSchema().getObjectType(src);
                    }
                    return null;
                })
            .build();
    }

    @Bean
    public RuntimeWiring buildDataFetcherWiring(List<DataFetcherWrapper<?>> dataFetcherWrappers) {
        RuntimeWiring.Builder builder = buildDataFetcherWiringRecursively(
            RuntimeWiring.newRuntimeWiring(),
            io.vavr.collection.List.ofAll(dataFetcherWrappers)
        ).invoke();

        return builder.build();
    }

    private TailCall<RuntimeWiring.Builder> buildDataFetcherWiringRecursively(RuntimeWiring.Builder runtimeWiringBuilder,
                                                                      io.vavr.collection.List<DataFetcherWrapper<?>> vavrList) {
        if (vavrList.isEmpty()) {
            return done(runtimeWiringBuilder);
        } else {
            DataFetcherWrapper<?> wrapper = vavrList.head();
            return () -> buildDataFetcherWiringRecursively(
                runtimeWiringBuilder
                    .type(newTypeWiring(wrapper.getParentType())
                        .dataFetcher(wrapper.getFieldName(), wrapper.getDataFetcher())),
                vavrList.tail()
            );
        }
    }
}
