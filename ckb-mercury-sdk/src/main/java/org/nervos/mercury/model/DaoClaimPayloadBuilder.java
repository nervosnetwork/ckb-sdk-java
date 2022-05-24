package org.nervos.mercury.model;

import org.nervos.mercury.FeeConstant;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.payload.DaoClaimPayload;

public class DaoClaimPayloadBuilder extends DaoClaimPayload {

  public DaoClaimPayloadBuilder() {
    this.feeRate = FeeConstant.DEFAULT_FEE_RATE;
  }

  public void setFrom(Item item) {
    this.from = item;
  }

  public void setTo(String address) {
    this.to = address;
  }

  public void setFeeRate(Long feeRate) {
    this.feeRate = feeRate;
  }

  public DaoClaimPayload build() {
    return this;
  }
}
