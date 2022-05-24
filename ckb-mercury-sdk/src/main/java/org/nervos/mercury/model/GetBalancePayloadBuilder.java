package org.nervos.mercury.model;

import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.payload.GetBalancePayload;

import java.util.HashSet;

public class GetBalancePayloadBuilder extends GetBalancePayload {

  public GetBalancePayloadBuilder() {
    this.assetInfos = new HashSet<>(2, 1);
  }

  public void setItem(Item item) {
    this.item = item;
  }

  public void addAssetInfo(AssetInfo info) {
    if (this.assetInfos == null) {
      this.assetInfos = new HashSet<>();
    }
    this.assetInfos.add(info);
  }

  public void setTipBlockNumber(Long blockNum) {
    this.tipBlockNumber = blockNum;
  }

  public GetBalancePayload build() {
    return this;
  }
}
