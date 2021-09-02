package org.nervos.mercury.model.resp;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.math.BigInteger;

/** @author zjh @Created Date: 2021/7/20 @Description: @Modify by: */
public class RecordResponse
    implements JsonSerializer<RecordResponse>, JsonDeserializer<RecordResponse> {

  public Integer id;

  public String address;

  public BigInteger amount;

  public AssetInfo assetInfo;

  public AssetStatus status;

  public BigInteger blockNumber;

  @Override
  public RecordResponse deserialize(
      JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    RecordResponse record = new RecordResponse();

    JsonObject jsonObject = json.getAsJsonObject();

    if (fieldExist(jsonObject, "id")) {
      record.id = jsonObject.get("id").getAsInt();
    }
    if (fieldExist(jsonObject, "key_address")) {
      record.address = jsonObject.get("key_address").getAsString();
    }

    if (fieldExist(jsonObject, "amount")) {
      JsonObject amount = jsonObject.get("amount").getAsJsonObject();
      if (fieldExist(amount, "value")) {
        record.amount = amount.get("value").getAsBigInteger();
      }
      record.assetInfo = new AssetInfo();
      if (fieldExist(amount, "udt_hash")) {
        record.assetInfo.udtHash = amount.get("udt_hash").getAsString();
        record.assetInfo.assetType = AssetInfo.AssetType.UDT;
      } else {
        record.assetInfo.assetType = AssetInfo.AssetType.CKB;
      }
      if (fieldExist(amount, "status")) {
        JsonObject status = amount.get("status").getAsJsonObject();
        if (fieldExist(status, "claimable")) {
          record.status = AssetStatus.Claimable;
          record.blockNumber = status.get("claimable").getAsBigInteger();
        } else if (fieldExist(status, "fixed")) {
          record.status = AssetStatus.Fixed;
          record.blockNumber = status.get("fixed").getAsBigInteger();
        }
      }
    }
    return record;
  }

  private boolean fieldExist(JsonObject jsonObject, String fieldName) {
    if (jsonObject.has(fieldName) && !jsonObject.get(fieldName).isJsonNull()) {
      return true;
    }
    return false;
  }

  @Override
  public JsonElement serialize(
      RecordResponse src, Type typeOfSrc, JsonSerializationContext context) {
    return context.serialize(src, src.getClass());
  }
}
