package com.greenfrog.utils.datastore.utils;

import com.google.cloud.datastore.Blob;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class PropertyConversionUtils {

    private PropertyConversionUtils() {

    }

    public static <T> Blob convertToBlob(T type) {
        return Blob.copyFrom(ByteBufferUtils.serialize(type));
    }

    public static <T> T convertFromBlob(Blob blob, Class<T> clazz) {
        try {
            ByteArrayInputStream bos = new ByteArrayInputStream(blob.toByteArray());
            ObjectInputStream is = new ObjectInputStream(bos);
            return (T) is.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
