package com.greenfrog.utils.datastore.mapper.mapper.properties;

import com.google.cloud.datastore.LongValue;
import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.Value;
import com.greenfrog.utils.datastore.utils.AnnotationUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class PropertyGenerator {

    private final Map<String, Value<?>> propertyMap;

    public PropertyGenerator(Map<String, Value<?>> propertyMap) {
        this.propertyMap = propertyMap;
    }

    public void generateProperty(Field field) {
        switch (AnnotationUtils.getAutoGenerationType(field)) {
            case NOW:  generateCurrentTimestamp(field);   break;
            case UUID: generateUUID(field);               break;
            default: break;
        }
    }

    private void generateCurrentTimestamp(Field field) {
        propertyMap.put(AnnotationUtils.getColumnValue(field), LongValue.of(new Date().getTime()));
    }

    private void generateUUID(Field field) {
        propertyMap.put(AnnotationUtils.getColumnValue(field), StringValue.of(UUID.randomUUID().toString()));
    }
}
