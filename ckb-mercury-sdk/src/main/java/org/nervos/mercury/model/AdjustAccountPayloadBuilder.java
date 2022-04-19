package org.nervos.mercury.model;

import java.math.BigInteger;
import java.util.HashSet;

import org.nervos.mercury.FeeConstant;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.payload.AdjustAccountPayload;

public class AdjustAccountPayloadBuilder extends AdjustAccountPayload {

  public AdjustAccountPayloadBuilder() {
    this.feeRate = FeeConstant.DEFAULT_FEE_RATE;
    this.from = new HashSet<>(2, 1);
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

  public void accountNumber(BigInteger accountNumber) {
    this.accountNumber = accountNumber;
  }

  public void extraCkb(long extraCkb) {
    this.extraCkb = extraCkb;
  }

  public void feeRate(BigInteger feeRate) {
    this.feeRate = feeRate;
  }

  public AdjustAccountPayload build() {
    return this;
  }
}
