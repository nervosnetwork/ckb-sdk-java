package org.nervos.mercury.model.common;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.math.BigInteger;
import org.nervos.mercury.model.resp.AssetStatus;

public class RecordStatus implements JsonSerializer<RecordStatus>, JsonDeserializer<RecordStatus> {

  public AssetStatus status;

  public BigInteger blockNumber;

  public RecordStatus() {}

  public RecordStatus(AssetStatus status, BigInteger blockNumber) {
    this.status = status;
    this.blockNumber = blockNumber;
  }

  @Override
  public RecordStatus deserialize(
      JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    if (json.getAsJsonObject().has("Claimable")) {
      BigInteger blockNumber = json.getAsJsonObject().get("Claimable").getAsBigInteger();
      return new RecordStatus(AssetStatus.Claimable, blockNumber);
    } else {
      BigInteger blockNumber = json.getAsJsonObject().get("Fixed").getAsBigInteger();
      return new RecordStatus(AssetStatus.Fixed, blockNumber);
    }
  }

  @Override
  public JsonElement serialize(RecordStatus src, Type typeOfSrc, JsonSerializationContext context) {
    return context.serialize(src, src.getClass());
  }
}
