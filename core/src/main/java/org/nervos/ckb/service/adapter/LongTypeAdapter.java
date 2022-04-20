package org.nervos.ckb.service.adapter;

import com.google.gson.*;

import java.lang.reflect.Type;

public class LongTypeAdapter implements JsonDeserializer<Long>, JsonSerializer<Long> {

  @Override
  public JsonElement serialize(Long src, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive("0x" + Long.toHexString(src));
  }

  @Override
  public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    if (json.getAsJsonPrimitive().isNumber()) {
      return json.getAsLong();
    }
    String hexValue = json.getAsString().substring(2);
    return Long.parseUnsignedLong(hexValue, 16);
  }
}
