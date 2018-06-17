package social.gripp.utils.entitymapper.mapper;

import social.gripp.utils.entitymapper.annotations.Column;
import social.gripp.utils.entitymapper.annotations.Key;
import social.gripp.utils.entitymapper.annotations.Provided;
import social.gripp.utils.entitymapper.annotations.Store;
import com.google.cloud.datastore.*;
import social.gripp.utils.entitymapper.entity.EntityUtils;
import social.gripp.utils.entitymapper.enums.EnumDescription;
import social.gripp.utils.entitymapper.types.DataType;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntityMapper<BEAN>  {

    private Map<String, Value<?>> propertyMap = new HashMap<>();
    private Datastore datastore = new DatastoreOptions.DefaultDatastoreFactory().create(DatastoreOptions.getDefaultInstance());
    private FullEntity.Builder entityBuilder;

    public FullEntity<IncompleteKey> mapBeanToEntity(final BEAN bean, MapperCallback callback) {
        KeyFactory keyFactory = datastore.newKeyFactory().setKind(setStoreName(bean).orElse(null));
        entityBuilder = FullEntity.newBuilder(keyFactory.newKey());

        mapBeanValues(bean);
        callback.mapProvidedFields(propertyMap, bean);

        propertyMap.forEach((key, value) -> entityBuilder.set(key, value));

        return Entity.newBuilder(entityBuilder.build()).build();
    }

    private Optional<String> setStoreName(final BEAN bean) {
        return Optional.ofNullable(bean.getClass().getAnnotation(Store.class).value());
    }

    private void mapBeanValues(final BEAN bean) {
            Stream.of(bean.getClass().getDeclaredFields())
                    .filter(this::shouldMapFieldToProperty)
                    .forEach(field -> {
                        if (field.getType().isEnum()) {
                            tryToSetEntityEnumProperty(field, bean);
                        }
                        else {
                            tryToSetEntityProperty(field, bean);
                        }
                    });
    }

    private boolean shouldMapFieldToProperty(Field field) {
        return field.getAnnotation(Column.class) != null
                && field.getAnnotation(Key.class) == null
                && !isProvidedOnIn(field);
    }

    private boolean isProvidedOnIn(Field field) {
        return field.getAnnotation(Provided.class) != null
                && field.getAnnotation(Provided.class).in();
    }

    private void tryToSetEntityEnumProperty(Field field, final  BEAN bean) {
        field.setAccessible(true);

        try {
            Enum enumField = (Enum) field.get(bean);

            if (enumField != null) {
                setPropertyIfPresent(
                        field.getAnnotation(Column.class).value(),
                        enumField instanceof EnumDescription ? ((EnumDescription) enumField).getDescription() : enumField.name(),
                        DataType.STRING);
            }
        }
        catch (IllegalAccessException ex) {

        }
    }

    private void tryToSetEntityProperty(Field field, final BEAN bean) {
        field.setAccessible(true);

        try {
            setPropertyIfPresent(
                    field.getAnnotation(Column.class).value(),
                    field.get(bean),
                    field.getAnnotation(Column.class).dataType());
        }
        catch (IllegalAccessException ex) {

        }
    }

    public BEAN mapEntityToBean(FullEntity<?> entity, BEAN bean) {
        Map<String, Value<?>> propertyMap = entity.getNames().stream()
                .filter(entity::contains)
                .collect(Collectors.toMap(
                        String::toString,
                        entity::getValue
                ));

        Stream.of(bean.getClass().getDeclaredFields())
                .filter(field -> shouldMapPropertyToField(field, propertyMap))
                .forEach(field -> {
                    field.setAccessible(true);

                    if (isKeyField(field)) {
                        setKeyField(field, bean, propertyMap);
                    }
                    else if (field.getType().isEnum()) {
                        setEnumField(field, bean, propertyMap);
                    }
                    else {
                        setField(field, bean, propertyMap);
                    }
                });

        return bean;
    }

    private boolean shouldMapPropertyToField(Field field, Map<String, Value<?>> propertyMap) {
        return field.getAnnotation(Column.class) != null
                && !isProvidedOnOut(field)
                && propertyMap.containsKey(field.getAnnotation(Column.class).value());
    }

    private boolean isProvidedOnOut(Field field) {
        return field.getAnnotation(Provided.class) != null
                && field.getAnnotation(Provided.class).out();
    }

    private boolean isKeyField(Field field) {
        return field.getAnnotation(Key.class) != null;
    }

    private void setKeyField(Field field, BEAN bean, Map<String, Value<?>> propertyMap) {
        field.setAccessible(true);

        try {
            field.set(bean, EntityUtils.convertKey(propertyMap.get(field.getAnnotation(Column.class).value())));
        }
        catch (IllegalAccessException ex) {

        }
    }

    private void setEnumField(Field field, BEAN bean, Map<String, Value<?>> propertyMap) {
        field.setAccessible(true);

        try {
            String value = (String) propertyMap.get(field.getAnnotation(Column.class).value()).get();

            for (Enum enums : (Enum[]) field.getType().getEnumConstants()) {
                if (enums instanceof EnumDescription && matchesEnumDesription((EnumDescription) enums, value)) {
                    field.set(bean, enums);
                    return;
                }
            }
        }
        catch (IllegalAccessException ex) {

        }
    }

    private boolean matchesEnumDesription(EnumDescription enumDescription, String value) {
        return enumDescription.hasDescription(value);
    }

    private void setField(Field field, BEAN bean, Map<String, Value<?>> propertyMap) {
        field.setAccessible(true);
        DataType dataType = field.getAnnotation(Column.class).dataType();

        try {
            switch (dataType) {
                case STRING: field.set(bean, (String) propertyMap.get(field.getAnnotation(Column.class).value()).get());    break;
                case LONG: field.set(bean, (Long) propertyMap.get(field.getAnnotation(Column.class).value()).get());        break;
            }
        }
        catch (IllegalAccessException ex) {

        }
    }

    public void setPropertyIfPresent(String property, Object object, DataType dataType) {
        if (object != null) {
            switch (dataType) {
                case STRING: propertyMap.put(property, StringValue.of((String) object));    break;
                case LONG: propertyMap.put(property, LongValue.of((Long) object));          break;
            }
        }
    }
}
