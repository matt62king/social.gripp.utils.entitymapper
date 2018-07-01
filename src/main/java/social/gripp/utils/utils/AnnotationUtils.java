package social.gripp.utils.utils;

import social.gripp.utils.fecher.annotaions.Join;
import social.gripp.utils.mapper.annotations.Column;
import social.gripp.utils.mapper.annotations.EntityKey;
import social.gripp.utils.mapper.annotations.Provided;
import social.gripp.utils.mapper.annotations.Store;
import social.gripp.utils.mapper.types.DataType;

import java.lang.reflect.Field;

public class AnnotationUtils {

    public static <T> String getStoreName(Class<T> clazz) {
        return clazz.getAnnotation(Store.class).value();
    }

    public static <T> String getJoinedIndex(Class<T> clazz) {
        return clazz.getAnnotation(Join.class).index();
    }

    public static <T> String[] getJoinedStores(Class<T> clazz) {
        return clazz.getAnnotation(Join.class).stores();
    }

    public static String getColumnValue(Field field) {
        return field.getAnnotation(Column.class).value();
    }

    public static DataType getColumnDataType(Field field) {
        return field.getAnnotation(Column.class).dataType();
    }

    public static boolean isKeyField(Field field) {
        return field.getAnnotation(EntityKey.class) != null;
    }

    public static boolean isProvidedOnIn(Field field) {
        return field.getAnnotation(Provided.class) != null && field.getAnnotation(Provided.class).in();
    }

    public static boolean isProvidedOnOut(Field field){
        return field.getAnnotation(Provided.class) != null && field.getAnnotation(Provided.class).out();
    }
}
