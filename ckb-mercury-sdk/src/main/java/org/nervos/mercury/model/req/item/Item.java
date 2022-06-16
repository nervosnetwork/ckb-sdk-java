package org.nervos.mercury.model.req.item;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import org.nervos.ckb.type.OutPoint;

@JsonAdapter(Item.Serializer.class)
public class Item {
  Type type;
  Object value;

  public Item(Type type, Object value) {
    this.type = type;
    this.value = value;
  }

  public enum Type {
    @SerializedName("Identity")
    IDENTITY,
    @SerializedName("Address")
    ADDRESS,
    @SerializedName("OutPoint")
    OUT_POINT
  }

  public static class Serializer implements JsonSerializer<Item> {
    @Override
    public JsonElement serialize(
        Item src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
      JsonObject obj = new JsonObject();
      obj.add("type", context.serialize(src.type, src.type.getClass()));
      JsonElement value;
      switch (src.type) {
        case IDENTITY:
        case ADDRESS:
          value = context.serialize(src.value, String.class);
          break;
        case OUT_POINT:
          value = context.serialize(src.value, OutPoint.class);
          break;
        default:
          throw new IllegalStateException("Unknown type");
      }
      obj.add("value", value);
      return obj;
    }
  }
}
