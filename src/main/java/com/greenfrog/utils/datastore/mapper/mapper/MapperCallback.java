package com.greenfrog.utils.datastore.mapper.mapper;

import com.google.cloud.datastore.Value;

import java.util.Map;

public interface MapperCallback<BEAN> {
    default void mapProvidedFields(Map<String, Value<?>> propertyMap, BEAN bean) {}
    default void mapProvidedProperties(Map<String, Value<?>> propertyMap, BEAN bean) {}
}
