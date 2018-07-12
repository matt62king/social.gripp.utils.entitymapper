package com.greenfrog.utils.datastore.mapper.mapper;


import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;

public abstract class Mapper<BEAN> implements MapperCallback<BEAN> {

    private final EntityMapper<BEAN> entityMapper;

    protected Mapper() {
        this.entityMapper = new EntityMapper<>();
    }

    public EntityMapper<BEAN> getEntityMapper() {
        return entityMapper;
    }

    public abstract FullEntity<IncompleteKey> mapEntity(BEAN bean);
    public abstract BEAN mapBean(FullEntity<?> entity);
}
