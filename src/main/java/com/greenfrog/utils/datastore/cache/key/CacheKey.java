package com.greenfrog.utils.datastore.cache.key;

public class CacheKey {

    private final String store;
    private final String query;

    public CacheKey(String store, String query) {
        this.store = store;
        this.query = query;
    }

    public String getStore() {
        return store;
    }

    public String getQuery() {
        return query;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CacheKey cacheKey = (CacheKey) o;

        if (store != null ? !store.equals(cacheKey.store) : cacheKey.store != null) return false;
        return query != null ? query.equals(cacheKey.query) : cacheKey.query == null;
    }

    @Override
    public int hashCode() {
        int result = store != null ? store.hashCode() : 0;
        result = 31 * result + (query != null ? query.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CacheKey{" +
                "store='" + store + '\'' +
                ", query='" + query + '\'' +
                '}';
    }
}
