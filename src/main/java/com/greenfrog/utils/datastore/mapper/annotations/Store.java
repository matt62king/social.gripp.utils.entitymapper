package com.greenfrog.utils.datastore.mapper.annotations;

import com.greenfrog.utils.datastore.mapper.mapper.DefaultMapper;
import com.greenfrog.utils.datastore.mapper.mapper.Mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Store {
    String value();
    boolean cache() default false;
    Class<? extends Mapper> mapper() default DefaultMapper.class;
}
