package com.test.graphql.configuration;

import com.test.graphql.datafetcher.NamedDataLoader;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderFactory;
import org.dataloader.DataLoaderRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Supplier;

@Configuration
@RequiredArgsConstructor
public class DatafetcherConfiguration {

    @Bean
    public Supplier<DataLoaderRegistry> dataLoaderRegistrySupplier(List<NamedDataLoader<?,?>> namedDataLoaders) {
        return () -> {
            DataLoaderRegistry registry = new DataLoaderRegistry();
            namedDataLoaders
                .forEach(dl -> {
                    DataLoader<?, ?> dataLoader = DataLoaderFactory.newMappedDataLoader(dl.getSupplier().get());
                    registry.register(dl.getName(), dataLoader);
                });
            return registry;
        };
    }
}
