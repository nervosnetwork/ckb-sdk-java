package org.nervos.mercury.model;

import org.nervos.mercury.FeeConstant;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.payload.DaoClaimPayload;

import java.util.ArrayList;

public class DaoClaimPayloadBuilder extends DaoClaimPayload {

  public DaoClaimPayloadBuilder() {
    this.feeRate = FeeConstant.DEFAULT_FEE_RATE;
    this.from = new ArrayList<>();
  }

  public void addFrom(Item item) {
    this.from.add(item);
  }

  public void to(String address) {
    this.to = address;
  }

  public void feeRate(Long feeRate) {
    this.feeRate = feeRate;
  }

  public DaoClaimPayload build() {
    return this;
  }
}
