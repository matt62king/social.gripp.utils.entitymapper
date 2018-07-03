package com.greenfrog.utils.datastore.exceptions;

public class InvalidMapperException extends RuntimeException {

    public InvalidMapperException(Class clazz) {
        super("Unable to create mapper call instance for " + clazz.getName());
    }
}
