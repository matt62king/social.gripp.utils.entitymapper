package social.gripp.utils.fecher;

import com.google.cloud.datastore.*;

public class SimpleFetcher extends Fetcher {

    public SimpleFetcher(Datastore datastore) {
        super(datastore);
    }

    public QueryResults<Entity> fetchBySingleProperty(String storeName, String property, String value) {
        Query<Entity> clientQuery = Query.newEntityQueryBuilder()
                .setKind(storeName)
                .setFilter(StructuredQuery.PropertyFilter.eq(property, value))
                .build();

        return getDatastore().run(clientQuery);
    }
}
