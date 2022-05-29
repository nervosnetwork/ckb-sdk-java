package org.nervos.ckb.service.adapter;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class CheckedEnumTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (!type.getRawType().isEnum()) {
            return null;
        }

        TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);

        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }

                String rawValue = in.nextString();
                T value = delegate.fromJsonTree(new JsonPrimitive(rawValue));
                // By default, Gson return null for enum from unknown string.
                // Here we only return null when json is null.
                if (value == null) {
                    throw new JsonParseException(String.format("Undefined value '%s' for enum '%s'", rawValue, type));
                }
                return value;
            }
        };
    }
}
