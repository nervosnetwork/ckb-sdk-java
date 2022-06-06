package org.nervos.mercury.model;

import org.nervos.mercury.FeeConstant;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.payload.DaoWithdrawPayload;

import java.util.ArrayList;

public class DaoWithdrawPayloadBuilder extends DaoWithdrawPayload {

  public DaoWithdrawPayloadBuilder() {
    this.feeRate = FeeConstant.DEFAULT_FEE_RATE;
    this.from = new ArrayList<>();
  }

  public void addFrom(Item item) {
    this.from.add(item);
  }

  public void feeRate(Long feeRate) {
    this.feeRate = feeRate;
  }

  public DaoWithdrawPayload build() {
    return this;
  }
}
