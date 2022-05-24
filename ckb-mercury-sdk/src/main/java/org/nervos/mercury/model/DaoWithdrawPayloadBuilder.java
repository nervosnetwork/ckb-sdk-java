package org.nervos.mercury.model;

import org.nervos.mercury.FeeConstant;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.payload.DaoWithdrawPayload;

public class DaoWithdrawPayloadBuilder extends DaoWithdrawPayload {

  public DaoWithdrawPayloadBuilder() {
    this.feeRate = FeeConstant.DEFAULT_FEE_RATE;
  }

  public void setFrom(Item item) {
    this.from = item;
  }

  public void setPayFee(String address) {
    this.payFee = address;
  }

  public void setFeeRate(Long feeRate) {
    this.feeRate = feeRate;
  }

  public DaoWithdrawPayload build() {
    return this;
  }
}
