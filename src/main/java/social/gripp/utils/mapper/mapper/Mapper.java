package social.gripp.utils.mapper.mapper;


import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;

public abstract class Mapper<BEAN> implements MapperCallback<BEAN> {
    public abstract FullEntity<IncompleteKey> mapEntity(BEAN bean);
    public abstract BEAN mapBean(FullEntity<?> entity);
}
