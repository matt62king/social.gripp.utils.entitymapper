package social.gripp.utils.utils;

import social.gripp.utils.mapper.annotations.Store;

public class AnnotationUtils {

    public static <T> String getStoreName(Class<T> clazz) {
        return clazz.getAnnotation(Store.class).value();
    }
}
