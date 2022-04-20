package org.nervos.ckb.service.adapter;

import com.google.gson.*;

import java.lang.reflect.Type;

public class IntegerTypeAdapter implements JsonSerializer<Integer>, JsonDeserializer<Integer> {

  @Override
  public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive("0x" + Integer.toHexString(src));
  }

  @Override
  public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    if (json.getAsJsonPrimitive().isNumber()) {
      return json.getAsInt();
    }
    String hexValue = json.getAsString().substring(2);
    return Integer.parseUnsignedInt(hexValue, 16);
  }
}
