package org.nervos.ckb.service.adapter;

import com.google.gson.*;
import org.bouncycastle.util.encoders.Hex;
import org.nervos.ckb.utils.Numeric;

import java.lang.reflect.Type;

public class ByteArrayTypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {

    @Override
    public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            return null;
        }
        String value = AdapterUtils.toHexStringPadded(src);
        return new JsonPrimitive(value);
    }

    @Override
    public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Numeric.hexStringToByteArray(json.getAsString());
    }
}
