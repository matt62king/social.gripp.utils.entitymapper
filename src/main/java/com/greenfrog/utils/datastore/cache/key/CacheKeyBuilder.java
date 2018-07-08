package com.greenfrog.utils.datastore.cache.key;

import com.google.cloud.datastore.StructuredQuery;
import com.greenfrog.utils.datastore.mapper.annotations.Store;

import java.util.stream.Stream;

public class CacheKeyBuilder {

    private CacheKeyBuilder() {

    }

    public static <T> CacheKey buildCacheKey(Class<T> clazz, String queryString) {
        return new CacheKey(clazz.getAnnotation(Store.class).value(), queryString);
    }

    public static <T> CacheKey buildCacheKey(Class<T> clazz, StructuredQuery.PropertyFilter filter, StructuredQuery.PropertyFilter... filters) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(filter.toString());

        Stream.of(filters).forEach(filter1 -> stringBuilder.append(filter1.toString()));

        return new CacheKey(clazz.getAnnotation(Store.class).value(), stringBuilder.toString());
    }
}
