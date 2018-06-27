package social.gripp.utils.fecher;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.QueryResults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiEntityFetcher extends Fetcher {
    private final SimpleFetcher simpleFetcher;

    private List<String> stores = new ArrayList<>();
    private String joinProperty;

    public MultiEntityFetcher(Datastore datastore) {
        super(datastore);

        this.simpleFetcher = new SimpleFetcher(datastore);
    }

    public MultiEntityFetcher fetchFrom(String... stores) {
        for (String store : stores) {
            this.stores.add(store);
        }

        return this;
    }

    public MultiEntityFetcher joinOn(String joinProperty) {
        this.joinProperty = joinProperty;
        return this;
    }

    public Map<String, QueryResults<Entity>> fetch(String value) {
        Map<String, QueryResults<Entity>> resultsMap = new HashMap<>();

        stores.stream().forEach(store -> {
            resultsMap.put(store, simpleFetcher.fetchBySingleProperty(store, joinProperty, value));
        });

        return resultsMap;
    }
}
