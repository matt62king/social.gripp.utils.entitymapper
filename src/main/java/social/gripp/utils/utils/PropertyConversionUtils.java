package social.gripp.utils.utils;

import com.google.cloud.datastore.Blob;

public class PropertyConversionUtils {

    public static <T> Blob convertToBlob(T type) {
        return Blob.copyFrom(ByteBufferUtils.serialize(type));
    }

    public static <T> T convertFromBlob(Blob blob, Class<T> clazz) {
        return ByteBufferUtils.deserialize(blob.asReadOnlyByteBuffer(), clazz);
    }
}
