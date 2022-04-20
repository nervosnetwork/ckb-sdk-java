package org.nervos.ckb.service.adapter;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.math.BigInteger;

public class BigIntegerTypeAdapter
    implements JsonSerializer<BigInteger>, JsonDeserializer<BigInteger> {

  @Override
  public JsonElement serialize(BigInteger src, Type typeOfSrc, JsonSerializationContext context) {
    if (src == null) {
      return null;
    }
    return new JsonPrimitive("0x" + src.toString(16));
  }

  @Override
  public BigInteger deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    String hexValue = json.getAsString().substring(2);
    return new BigInteger(hexValue, 16);
  }
}
