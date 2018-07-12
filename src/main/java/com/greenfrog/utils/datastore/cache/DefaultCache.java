package com.greenfrog.utils.datastore.cache;

import com.greenfrog.utils.datastore.cache.key.CacheKey;
import com.greenfrog.utils.datastore.utils.AnnotationUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultCache implements DatastoreCache<CacheKey, List<?>> {

    private final Map<CacheKey, List<?>> underlyingMap = new HashMap<>();

    @Override
    public void put(CacheKey key, List<?> objects) {
        underlyingMap.put(key, objects);
    }

    @Override
    public List<?> get(CacheKey key) {
        return underlyingMap.get(key);
    }

    @Override
    public boolean hasCache(CacheKey key) {
        return underlyingMap.containsKey(key);
    }

    @Override
    public void clear(CacheKey key) {
        underlyingMap.remove(key);
    }

    @Override
    public <T> void clearEntity(Class<T> entity) {
        String storeName = AnnotationUtils.getStoreName(entity);

        underlyingMap.keySet().stream()
                .filter(cacheKey -> cacheKey.getStore().equals(storeName))
                .forEach(this::clear);
    }

    @Override
    public void clearAll() {
        underlyingMap.clear();
    }
}
