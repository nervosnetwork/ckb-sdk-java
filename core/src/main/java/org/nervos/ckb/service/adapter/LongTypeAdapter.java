package org.nervos.ckb.service.adapter;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.math.BigInteger;
import org.nervos.ckb.utils.Numeric;

public class LongTypeAdapter implements JsonDeserializer<Long>, JsonSerializer<Long> {

  @Override
  public JsonElement serialize(Long src, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(
        AdapterUtils.toHexStringForNumber(BigInteger.valueOf(src).toByteArray()));
  }

  @Override
  public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    if (json.getAsJsonPrimitive().isNumber()) {
      return json.getAsLong();
    }
    return Numeric.toBigInt(json.getAsString()).longValue();
  }
}
