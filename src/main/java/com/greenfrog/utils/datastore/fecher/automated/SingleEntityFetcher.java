package com.greenfrog.utils.datastore.fecher.automated;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery;
import com.greenfrog.utils.datastore.cache.DatastoreCache;
import com.greenfrog.utils.datastore.cache.annotaions.Cache;
import com.greenfrog.utils.datastore.cache.key.CacheKey;
import com.greenfrog.utils.datastore.cache.key.CacheKeyBuilder;
import com.greenfrog.utils.datastore.fecher.Fetcher;
import com.greenfrog.utils.datastore.fecher.manual.SimpleFetcher;
import com.greenfrog.utils.datastore.utils.AnnotationUtils;
import com.greenfrog.utils.datastore.utils.CacheUtils;
import com.greenfrog.utils.datastore.utils.MapperUtils;

import java.util.List;

public class SingleEntityFetcher extends Fetcher {

    public SingleEntityFetcher(Datastore datastore) {
        super(datastore, null);
    }

    public SingleEntityFetcher(Datastore datastore, DatastoreCache datastoreCache) {
        super(datastore, datastoreCache);
    }

    public <T> List<T> getByIndexedId(Class<T> clazz, String value) {
        return getBySingleProperty(clazz, MapperUtils.getIndexedId(clazz), value);

//        CacheKey cacheKey = CacheKeyBuilder.buildCacheKey(clazz, value);
//        List<T> results = CacheUtils.fetchResultsFromCache(clazz, cacheKey, getDatastoreCache());
//
//        if (results.isEmpty()) {
//            QueryResults<Entity> queryResults = fetchedByIndexedId(clazz, value);
//            results = MapperUtils.convertResultsToList(clazz, queryResults);
//        }
//
//        return results;
    }

//    public QueryResults<Entity> fetchedByIndexedId(Class bClass, String value) {
//        return new SimpleFetcher(getDatastore()).fetchBySingleProperty(
//                AnnotationUtils.getStoreName(bClass),
//                MapperUtils.getIndexedId(bClass),
//                value);
//    }

    public <T> List<T> getBySingleProperty(Class<T> clazz, String property, String value) {
        CacheKey cacheKey = CacheKeyBuilder.buildCacheKey(clazz, value);
        List<T> results = CacheUtils.fetchResultsFromCache(clazz, cacheKey, getDatastoreCache());

        if (results.isEmpty()) {
            QueryResults<Entity> queryResults = fetchedBySingleProperty(clazz, property, value);
            results = MapperUtils.convertResultsToList(clazz, queryResults);
        }

        return results;

//        QueryResults<Entity> queryResults = fetchedBySingleProperty(clazz, property, value);
//        return MapperUtils.convertResultsToList(clazz, queryResults);
    }

    public QueryResults<Entity> fetchedBySingleProperty(Class clazz, String property, String value) {
        return new SimpleFetcher(getDatastore()).fetchBySingleProperty(
                AnnotationUtils.getStoreName(clazz),
                property,
                value);
    }

    public <T> List<T> getByMulitpileProperties(Class<T> clazz, StructuredQuery.PropertyFilter filter, StructuredQuery.PropertyFilter... filters) {
        CacheKey cacheKey = CacheKeyBuilder.buildCacheKey(clazz, filter, filters);
        List<T> results = CacheUtils.fetchResultsFromCache(clazz, cacheKey, getDatastoreCache());

        if (results.isEmpty()) {
            QueryResults<Entity> queryResults = fetchedByMulitpileProperties(clazz, filter, filters);
            results = MapperUtils.convertResultsToList(clazz, queryResults);
        }

        return results;
    }

    private QueryResults<Entity> fetchedByMulitpileProperties(Class clazz, StructuredQuery.PropertyFilter filter, StructuredQuery.PropertyFilter... filters) {
        return new SimpleFetcher(getDatastore()).fetchByMulitpleProperties(
                AnnotationUtils.getStoreName(clazz),
                filter,
                filters);
    }
}
