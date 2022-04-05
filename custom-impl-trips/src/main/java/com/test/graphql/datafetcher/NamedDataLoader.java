package com.test.graphql.datafetcher;

import org.dataloader.MappedBatchLoader;

import java.util.function.Supplier;

public interface NamedDataLoader<K, V> extends MappedBatchLoader<K, V> {
    String getName();
    Supplier<NamedDataLoader<K, V>> getSupplier();
}
