package social.gripp.utils.utils;

import java.io.*;
import java.nio.ByteBuffer;

public class ByteBufferUtils {

    private ByteBufferUtils() {

    }

    public static ByteBuffer serialize(Object state) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(512);

        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(state);
            oos.flush();
            byte[] bytes = bos.toByteArray();
            return ByteBuffer.wrap(bytes);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <T> T deserialize(ByteBuffer byteBuffer, Class<T> clazz) {
        ByteArrayInputStream bais = new ByteArrayInputStream(byteBuffer.array());

        try (ObjectInputStream oip = new ObjectInputStream(bais)) {
            return (T) oip.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
