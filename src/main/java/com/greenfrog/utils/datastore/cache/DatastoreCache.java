package com.greenfrog.utils.datastore.cache;

import com.greenfrog.utils.datastore.cache.key.CacheKey;

public interface DatastoreCache<KEY extends CacheKey, ENTITY> {

    void put(KEY key, ENTITY entity);

    ENTITY get(KEY key);

    boolean hasCache(KEY key);

    void clear(KEY key);

    <T> void clearEntity(Class<T> entity);

    void clearAll();
}
