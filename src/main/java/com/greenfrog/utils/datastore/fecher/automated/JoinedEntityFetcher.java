package com.greenfrog.utils.datastore.fecher.automated;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.QueryResults;
import com.greenfrog.utils.datastore.cache.DatastoreCache;
import com.greenfrog.utils.datastore.cache.key.CacheKey;
import com.greenfrog.utils.datastore.cache.key.CacheKeyBuilder;
import com.greenfrog.utils.datastore.fecher.Fetcher;
import com.greenfrog.utils.datastore.fecher.manual.MultiEntityFetcher;
import com.greenfrog.utils.datastore.utils.AnnotationUtils;
import com.greenfrog.utils.datastore.utils.CacheUtils;
import com.greenfrog.utils.datastore.utils.MapperUtils;

import java.lang.reflect.Field;
import java.util.*;

public class JoinedEntityFetcher extends Fetcher {

    private final MultiEntityFetcher multiEntityFetcher;
    private final SingleEntityFetcher singleEntityFetcher;

    public JoinedEntityFetcher(Datastore datastore) {
        super(datastore, null);

        this.multiEntityFetcher = new MultiEntityFetcher(datastore);
        this.singleEntityFetcher = new SingleEntityFetcher(datastore);
    }

    public JoinedEntityFetcher(Datastore datastore, DatastoreCache datastoreCache) {
        super(datastore, datastoreCache);

        this.multiEntityFetcher = new MultiEntityFetcher(datastore);
        this.singleEntityFetcher = new SingleEntityFetcher(datastore, datastoreCache);
    }

    public <T> List<T> fetchEntityAndJoinedByIndexedId(Class<T> clazz, String value) {
        return fetchEntityAndJoinedByProperty(clazz, MapperUtils.getIndexedId(clazz), value);
    }

    public <T> List<T> fetchEntityAndJoinedByProperty(Class<T> clazz, String property, String value) {
        List<T> entities = singleEntityFetcher.getBySingleProperty(clazz, property, value);
        entities.forEach(entity -> populateToManyRelationships(entity, property, value));

        return entities;
    }

    private <T> void populateToManyRelationships(T entity, String property, String value) {
        MapperUtils.getToManyRelationalFields(entity.getClass())
                .forEach(field -> setRelationList(entity, field, property, value));
    }

    private <T> void setRelationList(T entity, Field field, String property, String value) {
        try {
            field.setAccessible(true);
            field.set(entity, singleEntityFetcher.getBySingleProperty(
                    AnnotationUtils.getToManyEntityClass(field),
                    property,
                    value));
        }
        catch (IllegalAccessException ex) {

        }
    }

    public Map<String, QueryResults<Entity>> fetchAllEntityAndJoins(Class clazz, String value) {
        Map<String, QueryResults<Entity>> queryResultsMap = new HashMap<>();

        queryResultsMap.put(
                AnnotationUtils.getStoreName(clazz),
                singleEntityFetcher.fetchedBySingleProperty(clazz, MapperUtils.getIndexedId(clazz), value));

        queryResultsMap.putAll(multiEntityFetcher
                .fetchFromStores(AnnotationUtils.getJoinedStores(clazz))
                .joinOn(AnnotationUtils.getJoinedIndex(clazz))
                .fetch(value));

        return queryResultsMap;
    }
}
