package org.nervos.mercury.model.resp;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;

public class Ownership {

  public Type type;
  public String value;

  public Ownership(Type type, String value) {
    this.type = type;
    this.value = value;
  }

  enum Type {
    @SerializedName("Address")
    ADDRESS,
    @SerializedName("LockHash")
    LOCK_HASH
  }

  public static class Deserializer implements JsonDeserializer<Ownership> {
    @Override
    public Ownership deserialize(
        JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {
      Type type = Type.valueOf(json.getAsJsonObject().get("type").getAsString().toUpperCase());
      String value = json.getAsJsonObject().get("value").getAsString();
      return new Ownership(type, value);
    }
  }
}
