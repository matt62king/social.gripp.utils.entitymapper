package com.greenfrog.utils.datastore.fecher.automated;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery;
import com.greenfrog.utils.datastore.fecher.Fetcher;
import com.greenfrog.utils.datastore.fecher.manual.SimpleFetcher;
import com.greenfrog.utils.datastore.utils.AnnotationUtils;
import com.greenfrog.utils.datastore.utils.MapperUtils;

import java.util.List;

public class SingleEntityFetcher extends Fetcher {

    public SingleEntityFetcher(Datastore datastore) {
        super(datastore);
    }

    public <T> List<T> getByIndexedId(Class<T> clazz, String value) {
        QueryResults<Entity> queryResults = fetchedByIndexedId(clazz, value);
        return MapperUtils.convertResultsToList(clazz, queryResults);
    }

    public QueryResults<Entity> fetchedByIndexedId(Class bClass, String value) {
        return new SimpleFetcher(getDatastore()).fetchBySingleProperty(
                AnnotationUtils.getStoreName(bClass),
                MapperUtils.getIndexedId(bClass),
                value);
    }

    public <T> List<T> getBySingleProperty(Class<T> clazz, String property, String value) {
        QueryResults<Entity> queryResults = fetchedBySinglePropery(clazz, property, value);
        return MapperUtils.convertResultsToList(clazz, queryResults);
    }

    public QueryResults<Entity> fetchedBySinglePropery(Class clazz, String property, String value) {
        return new SimpleFetcher(getDatastore()).fetchBySingleProperty(
                AnnotationUtils.getStoreName(clazz),
                property,
                value);
    }

    public <T> List<T> getByMulitpleProperties(Class<T> clazz, StructuredQuery.PropertyFilter filter, StructuredQuery.PropertyFilter... filters) {
        QueryResults<Entity> queryResults = fetchedByMulitpleProperties(clazz, filter, filters);
        return MapperUtils.convertResultsToList(clazz, queryResults);
    }

    private QueryResults<Entity> fetchedByMulitpleProperties(Class clazz, StructuredQuery.PropertyFilter filter, StructuredQuery.PropertyFilter... filters) {
        return new SimpleFetcher(getDatastore()).fetchByMulitpleProperties(
                AnnotationUtils.getStoreName(clazz),
                filter,
                filters);
    }
}
