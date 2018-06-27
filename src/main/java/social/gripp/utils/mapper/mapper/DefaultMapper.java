package social.gripp.utils.mapper.mapper;

import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Value;

import java.util.Map;

public class DefaultMapper<BEAN> extends Mapper<BEAN> {
    private final EntityMapper<BEAN> entityMapper = new EntityMapper<>();
    private final Class<BEAN> beanClass;

    public DefaultMapper(Class<BEAN> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public FullEntity<IncompleteKey> mapEntity(BEAN bean) {
        return entityMapper.mapBeanToEntity(bean);
    }

    @Override
    public BEAN mapBean(FullEntity<?> entity) {
        try {
            return entityMapper.mapEntityToBean(entity, beanClass.newInstance());
        }
        catch (Exception e) {

        }

        return null;
    }

    @Override
    public void mapProvidedFields(Map<String, Value<?>> propertyMap, BEAN bean) {

    }
}
