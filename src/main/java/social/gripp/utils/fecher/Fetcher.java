package social.gripp.utils.fecher;

import com.google.cloud.datastore.Datastore;

public abstract class Fetcher {
    private final Datastore datastore;

    protected Fetcher(Datastore datastore) {
        this.datastore = datastore;
    }

    public Datastore getDatastore() {
        return datastore;
    }
}
