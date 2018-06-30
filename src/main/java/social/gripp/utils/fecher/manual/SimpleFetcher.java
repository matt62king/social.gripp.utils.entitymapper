package social.gripp.utils.fecher.manual;

import com.google.cloud.datastore.*;
import social.gripp.utils.fecher.Fetcher;

public class SimpleFetcher extends Fetcher {

    public SimpleFetcher(Datastore datastore) {
        super(datastore);
    }

    public QueryResults<Entity> fetchBySingleProperty(String storeName, String property, String value) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(storeName)
                .setFilter(StructuredQuery.PropertyFilter.eq(property, value))
                .build();

        return getDatastore().run(query);
    }
}
