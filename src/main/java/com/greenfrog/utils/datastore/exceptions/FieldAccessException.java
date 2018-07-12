package com.greenfrog.utils.datastore.exceptions;

import java.lang.reflect.Field;

public class FieldAccessException extends RuntimeException {

    public FieldAccessException(Field field, Class clazz) {
        super("Could not access field " + field.getName() + " from class " + clazz.getName());
    }
}
