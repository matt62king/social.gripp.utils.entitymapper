package social.gripp.utils.exceptions;

public class NoIndexedIdException extends RuntimeException {

    public NoIndexedIdException(Class clazz) {
        super(clazz.getName() + " does not have any fields annotated with @IndexedID");
    }
}
