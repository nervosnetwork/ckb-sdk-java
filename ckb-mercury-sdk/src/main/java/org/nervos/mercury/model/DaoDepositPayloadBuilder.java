package org.nervos.mercury.model;

import org.nervos.mercury.FeeConstant;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.payload.DaoDepositPayload;

import java.util.ArrayList;

public class DaoDepositPayloadBuilder extends DaoDepositPayload {
  public DaoDepositPayloadBuilder() {
    this.feeRate = FeeConstant.DEFAULT_FEE_RATE;
    this.from = new ArrayList<>();
  }

  public void addFrom(Item from) {
    this.from.add(from);
  }

  public void to(String to) {
    this.to = to;
  }

  public void amount(long amount) {
    this.amount = amount;
  }

  public void feeRate(Long feeRate) {
    this.feeRate = feeRate;
  }

  public DaoDepositPayload build() {
    return this;
  }
}
