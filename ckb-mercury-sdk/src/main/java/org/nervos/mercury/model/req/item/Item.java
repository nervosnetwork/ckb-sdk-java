package org.nervos.mercury.model.req.item;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;

public class Item {
  Type type;
  Object value;

  protected Item(Type type, Object value) {
    this.type = type;
    this.value = value;
  }

  enum Type {
    @SerializedName("Identity")
    IDENTITY,
    @SerializedName("Address")
    ADDRESS,
    @SerializedName("Record")
    RECORD
  }

  public static class Serializer implements JsonSerializer<Item> {
    @Override
    public JsonElement serialize(
        Item src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
      JsonObject obj = new JsonObject();
      obj.add("type", context.serialize(src.type, src.type.getClass()));
      Object valueObj = null;
      switch (src.type) {
        case IDENTITY:
          valueObj = ((Identity) src.value).identity;
          break;
        case ADDRESS:
          valueObj = ((Address) src.value).address;
          break;
        case RECORD:
          valueObj = ((Record) src.value).record;
          break;
        default:
          throw new IllegalStateException("Unknown type");
      }
      obj.add("value", context.serialize(valueObj, String.class));
      return obj;
    }
  }
}
