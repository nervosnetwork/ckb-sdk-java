package org.nervos.mercury.model;

import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.payload.AccountInfoPayload;

public class AccountInfoPayloadBuilder extends AccountInfoPayload {

  public AccountInfoPayloadBuilder() {
  }

  public void item(Item item) {
    this.item = item;
  }

  public void assetInfo(AssetInfo assetInfo) {
    this.assetInfo = assetInfo;
  }

  public AccountInfoPayload build() {
    return this;
  }
}
