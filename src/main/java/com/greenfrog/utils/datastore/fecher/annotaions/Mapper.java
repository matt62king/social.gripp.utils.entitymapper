package com.greenfrog.utils.datastore.fecher.annotaions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mapper {
    Class<? extends com.greenfrog.utils.datastore.mapper.mapper.Mapper> value();
    String cacheKey() default "";
}
