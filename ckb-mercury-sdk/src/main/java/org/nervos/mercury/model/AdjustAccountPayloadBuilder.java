package org.nervos.mercury.model;

import org.nervos.mercury.FeeConstant;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.payload.AdjustAccountPayload;

import java.util.HashSet;

public class AdjustAccountPayloadBuilder extends AdjustAccountPayload {

  public AdjustAccountPayloadBuilder() {
    this.feeRate = FeeConstant.DEFAULT_FEE_RATE;
    this.from = new HashSet<>(2, 1);
  }

  public void setItem(Item item) {
    this.item = item;
  }

  public void addFrom(Item from) {
    this.from.add(from);
  }

  public void setAssetInfo(AssetInfo assetInfo) {
    this.assetInfo = assetInfo;
  }

  public void setAccountNumber(Integer accountNumber) {
    this.accountNumber = accountNumber;
  }

  public void setExtraCkb(Long extraCkb) {
    this.extraCkb = extraCkb;
  }

  public void setFeeRate(Long feeRate) {
    this.feeRate = feeRate;
  }

  public AdjustAccountPayload build() {
    return this;
  }
}
