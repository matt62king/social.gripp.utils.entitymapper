package social.gripp.utils.fecher;

import com.google.cloud.datastore.Datastore;

public abstract class Fetcher {
    private Datastore datastore;

    protected Fetcher(Datastore datastore) {
        this.datastore = datastore;
    }

    public Datastore getDatastore() {
        return datastore;
    }

    public void setDatastore(Datastore datastore) {
        this.datastore = datastore;
    }
}
