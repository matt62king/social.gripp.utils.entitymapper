package com.greenfrog.utils.datastore.mapper.mapper;

import com.greenfrog.utils.datastore.utils.PropertyConversionUtils;
import com.greenfrog.utils.datastore.mapper.annotations.Column;
import com.google.cloud.datastore.*;
import com.greenfrog.utils.datastore.mapper.enums.EnumDescription;
import com.greenfrog.utils.datastore.mapper.types.DataType;
import com.greenfrog.utils.datastore.utils.AnnotationUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntityMapper<BEAN>  {

    private Map<String, Value<?>> propertyMap = new HashMap<>();
    private Datastore datastore = new DatastoreOptions.DefaultDatastoreFactory().create(DatastoreOptions.getDefaultInstance());
    private FullEntity.Builder entityBuilder;

    public FullEntity<IncompleteKey> mapBeanToEntity(final BEAN bean) {
        return mapBeanToEntity(bean, null);
    }

    public FullEntity<IncompleteKey> mapBeanToEntity(final BEAN bean, @Nullable MapperCallback callback) {
        KeyFactory keyFactory = datastore.newKeyFactory().setKind(AnnotationUtils.getStoreName(bean.getClass()));
        entityBuilder = FullEntity.newBuilder(keyFactory.newKey());

        mapBeanValues(bean);

        if (callback != null) {
            callback.mapProvidedFields(propertyMap, bean);
        }

        propertyMap.forEach((key, value) -> entityBuilder.set(key, value));

        return Entity.newBuilder(entityBuilder.build()).build();
    }

    private void mapBeanValues(final BEAN bean) {
            Stream.of(bean.getClass().getDeclaredFields())
                    .filter(this::shouldMapFieldToProperty)
                    .forEach(field -> {
                        if (field.getType().isEnum()) {
                            tryToSetEntityEnumProperty(field, bean);
                        }
                        else {
                            tryToSetEntityProperty(field, bean);
                        }
                    });
    }

    private boolean shouldMapFieldToProperty(Field field) {
        return field.getAnnotation(Column.class) != null
                && !AnnotationUtils.isKeyField(field)
                && !AnnotationUtils.isProvidedOnIn(field);
    }

    private void tryToSetEntityEnumProperty(Field field, final  BEAN bean) {
        field.setAccessible(true);

        try {
            Enum enumField = (Enum) field.get(bean);

            if (enumField != null) {
                setPropertyIfPresent(
                        AnnotationUtils.getColumnValue(field),
                        enumField instanceof EnumDescription ? ((EnumDescription) enumField).getDescription() : enumField.name(),
                        DataType.STRING);
            }
        }
        catch (IllegalAccessException ex) {

        }
    }

    private void tryToSetEntityProperty(Field field, final BEAN bean) {
        field.setAccessible(true);

        try {
            setPropertyIfPresent(
                    AnnotationUtils.getColumnValue(field),
                    field.get(bean),
                    AnnotationUtils.getColumnDataType(field));
        }
        catch (IllegalAccessException ex) {

        }
    }

    public BEAN mapEntityToBean(FullEntity<?> entity, BEAN bean) {
        return mapEntityToBean(entity, bean, null);
    }

    public BEAN mapEntityToBean(FullEntity<?> entity, BEAN bean, @Nullable MapperCallback callback) {
        Map<String, Value<?>> propertyMap = entity.getNames().stream()
                .filter(entity::contains)
                .collect(Collectors.toMap(
                        String::toString,
                        entity::getValue
                ));

        Stream.of(bean.getClass().getDeclaredFields())
                .filter(field -> shouldMapPropertyToField(field, propertyMap))
                .forEach(field -> {
                    field.setAccessible(true);

                    if (AnnotationUtils.isKeyField(field)) {
                        setKeyField(field, bean, entity);
                    }
                    else if (field.getType().isEnum()) {
                        setEnumField(field, bean, propertyMap);
                    }
                    else {
                        setField(field, bean, propertyMap);
                    }
                });

        if (callback != null) {
            callback.mapProvidedProperties(propertyMap, bean);
        }

        return bean;
    }

    private boolean shouldMapPropertyToField(Field field, Map<String, Value<?>> propertyMap) {
        return (field.getAnnotation(Column.class) != null
                && !AnnotationUtils.isProvidedOnOut(field)
                && propertyMap.containsKey(AnnotationUtils.getColumnValue(field)))
                || AnnotationUtils.isKeyField(field);
    }

    private void setKeyField(Field field, BEAN bean, FullEntity entity) {
        field.setAccessible(true);

        try {
            field.set(bean, entity.getKey());
        }
        catch (IllegalAccessException ex) {

        }
    }

    private void setEnumField(Field field, BEAN bean, Map<String, Value<?>> propertyMap) {
        field.setAccessible(true);

        try {
            String value = (String) propertyMap.get(AnnotationUtils.getColumnValue(field)).get();

            for (Enum enums : (Enum[]) field.getType().getEnumConstants()) {
                if (enums instanceof EnumDescription && matchesEnumDesription((EnumDescription) enums, value)) {
                    field.set(bean, enums);
                    return;
                }
            }
        }
        catch (IllegalAccessException ex) {

        }
    }

    private boolean matchesEnumDesription(EnumDescription enumDescription, String value) {
        return enumDescription.hasDescription(value);
    }

    private void setField(Field field, BEAN bean, Map<String, Value<?>> propertyMap) {
        field.setAccessible(true);
        DataType dataType = AnnotationUtils.getColumnDataType(field);

        try {
            switch (dataType) {
                case BLOB: field.set(bean, PropertyConversionUtils.convertFromBlob(
                        (Blob) propertyMap.get(AnnotationUtils.getColumnValue(field)).get(), field.getType())); break;

                case STRING: field.set(bean, propertyMap.get(AnnotationUtils.getColumnValue(field)).get());     break;
                case LONG: field.set(bean, propertyMap.get(AnnotationUtils.getColumnValue(field)).get());       break;
            }
        }
        catch (IllegalAccessException ex) {

        }
    }

    public void setPropertyIfPresent(String property, Object object, DataType dataType) {
        if (object != null) {
            switch (dataType) {
                case BLOB: propertyMap.put(property, BlobValue.newBuilder(
                        PropertyConversionUtils.convertToBlob(object)).setExcludeFromIndexes(true).build());     break;

                case STRING: propertyMap.put(property, StringValue.of((String) object));                         break;
                case LONG: propertyMap.put(property, LongValue.of((Long) object));                               break;
            }
        }
    }
}
