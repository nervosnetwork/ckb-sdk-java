package org.nervos.mercury.model.resp;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import java.lang.reflect.Type;

public class AddressOrLockHash implements JsonDeserializer<AddressOrLockHash> {
  @SerializedName("address_or_lock_hash")
  public String addressOrLockHash;

  public AddressOrLockHashType type;

  public AddressOrLockHash() {}

  public AddressOrLockHash(String addressOrLockHash, AddressOrLockHashType type) {
    this.addressOrLockHash = addressOrLockHash;
    this.type = type;
  }

  @Override
  public AddressOrLockHash deserialize(
      JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    if (json.getAsJsonObject().has("Address")) {
      return new AddressOrLockHash(
          json.getAsJsonObject().get("Address").getAsString(), AddressOrLockHashType.Address);
    } else {
      return new AddressOrLockHash(
          json.getAsJsonObject().get("LockHash").getAsString(), AddressOrLockHashType.LockHash);
    }
  }
}
