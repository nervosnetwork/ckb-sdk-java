package org.nervos.mercury.model.req;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import org.nervos.ckb.address.AddressUtils;
import org.nervos.ckb.address.CodeHashType;

/** @author zjh @Created Date: 2021/7/17 @Description: @Modify by: */
public abstract class QueryAddress
    implements JsonSerializer<QueryAddress>, JsonDeserializer<QueryAddress> {
  public abstract String getAddress();

  @Override
  public QueryAddress deserialize(
      JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {

    if (json.getAsJsonObject().has("KeyAddress")) {
      return new KeyAddress(json.getAsJsonObject().get("KeyAddress").getAsString());
    } else {
      return new NormalAddress(json.getAsJsonObject().get("NormalAddress").getAsString());
    }
  }

  public static QueryAddress getQueryAddressByAddress(String address) {
    if (AddressUtils.parseAddressType(address) == CodeHashType.BLAKE160) {
      return new KeyAddress(address);
    }

    return new NormalAddress(address);
  }

  @Override
  public JsonElement serialize(QueryAddress src, Type typeOfSrc, JsonSerializationContext context) {
    return context.serialize(src, src.getClass());
  }
}
