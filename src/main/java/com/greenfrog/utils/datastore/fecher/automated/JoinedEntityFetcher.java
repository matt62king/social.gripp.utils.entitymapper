package com.greenfrog.utils.datastore.fecher.automated;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.QueryResults;
import com.greenfrog.utils.datastore.fecher.Fetcher;
import com.greenfrog.utils.datastore.fecher.manual.MultiEntityFetcher;
import com.greenfrog.utils.datastore.utils.AnnotationUtils;

import java.util.HashMap;
import java.util.Map;

public class JoinedEntityFetcher extends Fetcher {

    private final MultiEntityFetcher multiEntityFetcher;
    private final SingleEntityFetcher singleEntityFetcher;

    public JoinedEntityFetcher(Datastore datastore) {
        super(datastore);

        this.multiEntityFetcher = new MultiEntityFetcher(datastore);
        this.singleEntityFetcher = new SingleEntityFetcher(datastore);
    }

    public Map<String, QueryResults<Entity>> fetchAllEntityAndJoins(Class clazz, String value) {
        Map<String, QueryResults<Entity>> queryResultsMap = new HashMap<>();

        queryResultsMap.put(AnnotationUtils.getStoreName(clazz), singleEntityFetcher.fetchedByIndexedId(clazz, value));
        queryResultsMap.putAll(multiEntityFetcher
                .fetchFromStores(AnnotationUtils.getJoinedStores(clazz))
                .joinOn(AnnotationUtils.getJoinedIndex(clazz))
                .fetch(value));

        return queryResultsMap;
    }
}
