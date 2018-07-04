package com.greenfrog.utils.datastore.fecher.automated;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.QueryResults;
import com.greenfrog.utils.datastore.exceptions.InvalidMapperException;
import com.greenfrog.utils.datastore.exceptions.NoIndexedIdException;
import com.greenfrog.utils.datastore.fecher.Fetcher;
import com.greenfrog.utils.datastore.fecher.annotaions.IndexedID;
import com.greenfrog.utils.datastore.fecher.annotaions.MapConstructor;
import com.greenfrog.utils.datastore.fecher.manual.SimpleFetcher;
import com.greenfrog.utils.datastore.mapper.mapper.DefaultMapper;
import com.greenfrog.utils.datastore.mapper.mapper.Mapper;
import com.greenfrog.utils.datastore.utils.AnnotationUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SingleEntityFetcher extends Fetcher {

    public SingleEntityFetcher(Datastore datastore) {
        super(datastore);
    }

    public <T> List<T> getIndexdId(Class<T> clazz, String value) {
        QueryResults<Entity> queryResults = fetchedByIndexedId(clazz, value);
        Mapper<T> mapper = getMapperForClass(clazz);
        List<T> beanList = new ArrayList<>();

        while (queryResults.hasNext()) {
            beanList.add(mapper.mapBean(queryResults.next()));
        }

        return beanList;
    }

    private  <T> Mapper<T> getMapperForClass(Class<T> clazz) {
        try {
            if (AnnotationUtils.hasMapperClass(clazz)) {
                Class<? extends Mapper> mapperClass = AnnotationUtils.getMapperClass(clazz);
                Constructor constructor = getAnnotatedConstructor(mapperClass);

                return (Mapper<T>) constructor.newInstance();
            }
            else {
                return new DefaultMapper<>(clazz);
            }
        } catch (Exception ex) {
            throw new InvalidMapperException(clazz);
        }
    }

    private Constructor getAnnotatedConstructor(Class<?> clazz) {
        return Stream.of(clazz.getConstructors())
                .filter(constructor -> constructor.getDeclaredAnnotation(MapConstructor.class) != null)
                .findFirst().orElseThrow(() -> new InvalidMapperException(clazz));
    }

    public QueryResults<Entity> fetchedByIndexedId(Class bClass, String value) {
        return new SimpleFetcher(getDatastore()).fetchBySingleProperty(
                AnnotationUtils.getStoreName(bClass),
                getIndexedId(bClass),
                value);
    }

    private String getIndexedId(Class clazz) {
        Field fieldOptional = Stream.of(clazz.getDeclaredFields())
                .filter(AnnotationUtils::isIndexIdField)
                .findFirst().orElseThrow(() -> new NoIndexedIdException(clazz));

        return AnnotationUtils.getColumnValue(fieldOptional);
    }
}
