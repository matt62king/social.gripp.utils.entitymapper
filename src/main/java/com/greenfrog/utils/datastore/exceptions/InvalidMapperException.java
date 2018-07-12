package com.greenfrog.utils.datastore.exceptions;

public class InvalidMapperException extends RuntimeException {

    public InvalidMapperException(Class clazz) {
        super("Unable to create mapper instance for " + clazz.getName() + " ensure constructor is annotated with @MapConstructor");
    }
}
