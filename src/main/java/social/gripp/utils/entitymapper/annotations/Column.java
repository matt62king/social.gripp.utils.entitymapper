package social.gripp.utils.entitymapper.annotations;


import social.gripp.utils.entitymapper.types.DataType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    String value();
    DataType dataType() default DataType.STRING;
}
