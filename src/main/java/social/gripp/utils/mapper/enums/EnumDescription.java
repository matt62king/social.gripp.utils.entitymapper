package social.gripp.utils.mapper.enums;

public interface EnumDescription {
    String getDescription();

    default boolean hasDescription(String description) {
        return getDescription().equals(description);
    }
}
