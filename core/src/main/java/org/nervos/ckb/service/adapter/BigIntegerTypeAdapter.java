package org.nervos.ckb.service.adapter;

import com.google.gson.*;
import org.nervos.ckb.utils.Numeric;

import java.lang.reflect.Type;
import java.math.BigInteger;

public class BigIntegerTypeAdapter implements JsonSerializer<BigInteger>, JsonDeserializer<BigInteger> {

    @Override
    public JsonElement serialize(BigInteger src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            return null;
        }
        return new JsonPrimitive(AdapterUtils.toHexString(src.toByteArray()));
    }

    @Override
    public BigInteger deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new BigInteger(Numeric.hexStringToByteArray(json.getAsString()));
    }
}
