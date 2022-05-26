package org.nervos.mercury.model.common;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class DaoInfo {
  public DaoState state;
  public long reward;

  @JsonAdapter(DaoInfo.DaoState.Serializer.class)
  public static class DaoState {
    public Type type;
    public List<Long> value;

    public static class Serializer implements JsonSerializer<DaoState>, JsonDeserializer<DaoState> {

      @Override
      public JsonElement serialize(DaoState src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.add("type", context.serialize(src.type, Type.class));
        List<Long> value = src.value;
        if (value.size() == 1) {
          obj.add("value", context.serialize(value.get(0), Long.class));
        } else {
          obj.add("value", context.serialize(value, new TypeToken<List<Long>>() {}.getType()));
        }
        return obj;
      }

      @Override
      public DaoState deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        DaoState daoState = new DaoState();
        daoState.type = context.deserialize(obj.get("type"), Type.class);
        if (obj.get("value") instanceof JsonArray) {
          daoState.value = context.deserialize(obj.get("value"), new TypeToken<List<Long>>() {}.getType());
        } else {
          Long value = context.deserialize(obj.get("value"), Long.class);
          daoState.value = new ArrayList<>();
          daoState.value.add(value);
        }
        return daoState;
      }
    }

    public enum Type {
      @SerializedName("Deposit")
      DEPOSIT,
      @SerializedName("Withdraw")
      WITHDRAW,
    }
  }
}
