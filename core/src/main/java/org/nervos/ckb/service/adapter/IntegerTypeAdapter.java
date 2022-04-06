package org.nervos.ckb.service.adapter;

import com.google.gson.*;
import org.nervos.ckb.utils.Numeric;

import java.lang.reflect.Type;
import java.math.BigInteger;

public class IntegerTypeAdapter implements JsonSerializer<Integer>, JsonDeserializer<Integer> {

    @Override
    public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
        BigInteger value = BigInteger.valueOf(src);
        // serialize -1 to 0xffffffff for outpoint index in coinbase transaction
        if (src == -1) {
            return new JsonPrimitive("0xffffffff");
        } else {
            return new JsonPrimitive(AdapterUtils.toHexStringForNumber(value.toByteArray()));
        }
    }

    @Override
    public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.getAsJsonPrimitive().isNumber()) {
            return json.getAsInt();
        }
        BigInteger value = new BigInteger(Numeric.hexStringToByteArray(json.getAsString()));
        // deserialize 0xffffffff to -1 for outpoint index in coinbase transaction
        if (value.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) == 1) {
            return -1;
        } else {
            return value.intValue();
        }
    }
}
