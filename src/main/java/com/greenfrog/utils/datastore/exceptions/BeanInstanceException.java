package com.greenfrog.utils.datastore.exceptions;

public class BeanInstanceException extends RuntimeException {

    public BeanInstanceException(Class clazz) {
        super("Unable unable to create instance of " + clazz.getName());
    }
}
