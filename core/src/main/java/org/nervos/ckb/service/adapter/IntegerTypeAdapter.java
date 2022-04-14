package org.nervos.ckb.service.adapter;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.Objects;

public class IntegerTypeAdapter implements JsonSerializer<Integer>, JsonDeserializer<Integer> {

  @Override
  public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
    String hexValue;
    // serialize -1 to 0xffffffff for outpoint index in coinbase transaction
    if (src == -1) {
      hexValue = "ffffffff";
    } else {
      hexValue = Integer.toHexString(src);
    }
    return new JsonPrimitive("0x" + hexValue);
  }

  @Override
  public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    if (json.getAsJsonPrimitive().isNumber()) {
      return json.getAsInt();
    }
    String hexValue = json.getAsString().substring(2);
    // deserialize 0xffffffff to -1 for outpoint index in coinbase transaction
    if (Objects.equals("ffffffff", hexValue)) {
      return -1;
    } else {
      return Integer.valueOf(hexValue, 16);
    }
  }
}
