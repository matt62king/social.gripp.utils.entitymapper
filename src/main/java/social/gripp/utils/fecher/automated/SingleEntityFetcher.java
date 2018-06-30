package social.gripp.utils.fecher.automated;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.QueryResults;
import social.gripp.utils.exceptions.NoIndexedIdException;
import social.gripp.utils.fecher.Fetcher;
import social.gripp.utils.fecher.annotaions.IndexedID;
import social.gripp.utils.fecher.manual.SimpleFetcher;
import social.gripp.utils.mapper.annotations.Column;
import social.gripp.utils.utils.AnnotationUtils;

import java.lang.reflect.Field;
import java.util.stream.Stream;

public class SingleEntityFetcher extends Fetcher {

    public SingleEntityFetcher(Datastore datastore) {
        super(datastore);
    }

    public QueryResults<Entity> fetchedByIndexedId(Class bClass, String value) {
        return new SimpleFetcher(getDatastore()).fetchBySingleProperty(
                AnnotationUtils.getStoreName(bClass),
                getIndexedId(bClass),
                value);
    }

    private String getIndexedId(Class clazz) {
        Field fieldOptional = Stream.of(clazz.getDeclaredFields())
                .filter(field -> field.getAnnotation(IndexedID.class) != null)
                .findFirst().orElseThrow(() -> new NoIndexedIdException(clazz));

        return fieldOptional.getAnnotation(Column.class).value();
    }
}
