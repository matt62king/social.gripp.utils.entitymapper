package com.greenfrog.utils.datastore.fecher;

import com.google.cloud.datastore.Datastore;
import com.greenfrog.utils.datastore.cache.DatastoreCache;
import com.greenfrog.utils.datastore.cache.key.CacheKey;

public abstract class Fetcher {
    private final Datastore datastore;
    private final DatastoreCache datastoreCache;

    protected Fetcher(Datastore datastore, DatastoreCache datastoreCache) {
        this.datastore = datastore;
        this.datastoreCache = datastoreCache;
    }

    public Datastore getDatastore() {
        return datastore;
    }

    public DatastoreCache getDatastoreCache() {
        return datastoreCache;
    }

    public void clearCacheForKey(CacheKey cacheKey) {
        datastoreCache.clear(cacheKey);
    }

    public <T> void clearCacheForEntity(Class<T> entity) {
        datastoreCache.clearEntity(entity);
    }
}
