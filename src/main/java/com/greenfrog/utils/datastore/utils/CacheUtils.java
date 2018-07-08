package com.greenfrog.utils.datastore.utils;

import com.greenfrog.utils.datastore.cache.DatastoreCache;
import com.greenfrog.utils.datastore.cache.key.CacheKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CacheUtils {

    public static <T> List<T> fetchResultsFromCache(Class<T> clazz, CacheKey cacheKey, DatastoreCache datastoreCache) {
        Optional<List<T>> cacheResult = CacheUtils.fetchFromCache(cacheKey, datastoreCache);
        return cacheResult.orElse(new ArrayList<>());
    }

    private static Optional fetchFromCache(CacheKey cacheKey, DatastoreCache datastoreCache) {
        return Optional.ofNullable(datastoreCache.get(cacheKey));
    }

    public static void putInCache(CacheKey cacheKey, List<?> entity, DatastoreCache datastoreCache) {
        datastoreCache.put(cacheKey, entity);
    }
}
