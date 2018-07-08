package com.greenfrog.utils.datastore.utils;

import com.greenfrog.utils.datastore.cache.annotaions.Cache;
import com.greenfrog.utils.datastore.fecher.annotaions.IndexedID;
import com.greenfrog.utils.datastore.fecher.annotaions.Join;
import com.greenfrog.utils.datastore.fecher.annotaions.Mapper;
import com.greenfrog.utils.datastore.fecher.annotaions.ToMany;
import com.greenfrog.utils.datastore.mapper.annotations.Column;
import com.greenfrog.utils.datastore.mapper.annotations.EntityKey;
import com.greenfrog.utils.datastore.mapper.annotations.Provided;
import com.greenfrog.utils.datastore.mapper.annotations.Store;
import com.greenfrog.utils.datastore.mapper.mapper.DefaultMapper;
import com.greenfrog.utils.datastore.mapper.types.DataType;

import java.lang.reflect.Field;

public class AnnotationUtils {

    public static <T> String getStoreName(Class<T> clazz) {
        return clazz.getAnnotation(Store.class).value();
    }

    public static <T> String getJoinedIndex(Class<T> clazz) {
        return clazz.getAnnotation(Join.class).index();
    }

    public static <T> String[] getJoinedStores(Class<T> clazz) {
        return clazz.getAnnotation(Join.class).stores();
    }

    public static <T> Class<? extends com.greenfrog.utils.datastore.mapper.mapper.Mapper> getMapperClass(Class<T> clazz) {
        if (clazz.getAnnotation(Mapper.class) == null) {
            return clazz.getAnnotation(Store.class).mapper();
        }
        return clazz.getAnnotation(Mapper.class).value();
    }

    public static <T> boolean hasMapperClass(Class<T> clazz) {
        return clazz.getAnnotation(Mapper.class) != null
                || (clazz.getAnnotation(Store.class) != null
                && !clazz.getAnnotation(Store.class).mapper().getName().equals(DefaultMapper.class.getName()));
    }

    public static <T> boolean usesCache(Class<T> clazz) {
        return clazz.getAnnotation(Cache.class) != null ||
                (clazz.getAnnotation(Store.class) != null && !clazz.getAnnotation(Store.class).cache());
    }

    public static String getColumnValue(Field field) {
        return field.getAnnotation(Column.class).value();
    }

    public static DataType getColumnDataType(Field field) {
        return field.getAnnotation(Column.class).dataType();
    }

    public static <T> Class<T> getToManyEntityClass(Field field) {
        return field.getAnnotation(ToMany.class).value();
    }

    public static String getToManyJoinProperty(Field field) {
        return field.getAnnotation(ToMany.class).on();
    }

    public static boolean isKeyField(Field field) {
        return field.getAnnotation(EntityKey.class) != null;
    }

    public static boolean isIndexIdField(Field field) {
        return field.getAnnotation(IndexedID.class) != null;
    }

    public static boolean isProvidedOnIn(Field field) {
        return field.getAnnotation(Provided.class) != null && field.getAnnotation(Provided.class).in();
    }

    public static boolean isProvidedOnOut(Field field){
        return field.getAnnotation(Provided.class) != null && field.getAnnotation(Provided.class).out();
    }

    public static boolean isToManyRelation(Field field) {
        return field.getAnnotation(ToMany.class) != null;
    }
}
