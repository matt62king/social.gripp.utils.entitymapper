package com.greenfrog.utils.datastore.fecher.manual;

import com.google.cloud.datastore.*;
import com.greenfrog.utils.datastore.fecher.Fetcher;

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

    public QueryResults<Entity> fetchByMulitpleProperties(String storeName, StructuredQuery.PropertyFilter filter, StructuredQuery.PropertyFilter... filters) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(storeName)
                .setFilter(StructuredQuery.CompositeFilter.and(filter, filters))
                .build();

        return getDatastore().run(query);
    }
}
