package org.nervos.mercury;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.nervos.mercury.model.common.ExtraFilter;
import org.nervos.mercury.model.common.RecordStatus;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.resp.AddressOrLockHash;
import org.nervos.mercury.model.resp.RecordResponse;

public class GsonFactory {
  public static Gson newGson() {
    Gson g =
        new GsonBuilder()
            .registerTypeAdapter(AddressOrLockHash.class, new AddressOrLockHash())
            .registerTypeAdapter(RecordStatus.class, new RecordStatus())
            .registerTypeAdapter(RecordResponse.class, new RecordResponse())
            .registerTypeAdapter(ExtraFilter.class, new ExtraFilter())
            .registerTypeAdapter(Item.class, new Item.Serializer())
            .create();
    return g;
  }
}
