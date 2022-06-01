package org.nervos.mercury.model;

import org.nervos.mercury.FeeConstant;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.payload.AdjustAccountPayload;

import java.util.ArrayList;

public class AdjustAccountPayloadBuilder extends AdjustAccountPayload {

  public AdjustAccountPayloadBuilder() {
    this.feeRate = FeeConstant.DEFAULT_FEE_RATE;
    this.from = new ArrayList<>();
  }

  public void item(Item item) {
    this.item = item;
  }

  public void addFrom(Item from) {
    this.from.add(from);
  }

  public void assetInfo(AssetInfo assetInfo) {
    this.assetInfo = assetInfo;
  }

  public void accountNumber(Integer accountNumber) {
    this.accountNumber = accountNumber;
  }

  public void extraCkb(Long extraCkb) {
    this.extraCkb = extraCkb;
  }

  public void feeRate(Long feeRate) {
    this.feeRate = feeRate;
  }

  public AdjustAccountPayload build() {
    return this;
  }
}
