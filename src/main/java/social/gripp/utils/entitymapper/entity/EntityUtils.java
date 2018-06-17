package social.gripp.utils.entitymapper.entity;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class EntityUtils {

    private EntityUtils() {}

    public static Object convertKey(Object key) {
        return KeyFactory.keyToString((Key) key);
    }
}
