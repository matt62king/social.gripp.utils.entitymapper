package com.greenfrog.utils.datastore.utils;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.QueryResults;
import com.greenfrog.utils.datastore.exceptions.InvalidMapperException;
import com.greenfrog.utils.datastore.exceptions.NoIndexedIdException;
import com.greenfrog.utils.datastore.fecher.annotaions.MapConstructor;
import com.greenfrog.utils.datastore.mapper.annotations.EntityKey;
import com.greenfrog.utils.datastore.mapper.mapper.DefaultMapper;
import com.greenfrog.utils.datastore.mapper.mapper.Mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class MapperUtils {

    public static   <T> Mapper<T> getMapperForClass(Class<T> clazz) {
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

    private static Constructor getAnnotatedConstructor(Class<?> clazz) {
        return Stream.of(clazz.getConstructors())
                .filter(constructor -> constructor.getDeclaredAnnotation(MapConstructor.class) != null)
                .findFirst().orElseThrow(() -> new InvalidMapperException(clazz));
    }

    public static String getIndexedId(Class clazz) {
        Field fieldOptional = Stream.of(clazz.getDeclaredFields())
                .filter(AnnotationUtils::isIndexIdField)
                .findFirst().orElseThrow(() -> new NoIndexedIdException(clazz));

        return AnnotationUtils.getColumnValue(fieldOptional);
    }

    public static <T> List<T> convertResultsToList(Class<T> clazz, QueryResults<Entity> queryResults) {
        Mapper<T> mapper = getMapperForClass(clazz);
        List<T> beanList = new ArrayList<>();

        while (queryResults.hasNext()) {
            beanList.add(mapper.mapBean(queryResults.next()));
        }

        return beanList;
    }

    public static <B> Optional<Key> getKey(B bean) {
        Optional<Field> keyField = Stream.of(bean.getClass().getDeclaredFields())
                .filter(field -> field.getAnnotation(EntityKey.class) != null)
                .findFirst();

        try {
            if (keyField.isPresent()) {
                keyField.get().setAccessible(true);
                return Optional.ofNullable((Key) keyField.get().get(bean));
            }
            else {
                return Optional.empty();
            }
        }
        catch (IllegalAccessException ex) {

        }

        return Optional.empty();
    }
}
