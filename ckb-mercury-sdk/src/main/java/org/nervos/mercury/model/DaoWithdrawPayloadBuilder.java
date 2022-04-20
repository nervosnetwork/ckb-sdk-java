package org.nervos.mercury.model;

import org.nervos.mercury.FeeConstant;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.payload.DaoWithdrawPayload;

import java.math.BigInteger;

public class DaoWithdrawPayloadBuilder extends DaoWithdrawPayload {

  public DaoWithdrawPayloadBuilder() {
    this.feeRate = FeeConstant.DEFAULT_FEE_RATE;
  }

  public void from(Item item) {
    this.from = item;
  }

  public void payFee(String address) {
    this.payFee = address;
  }

  public void feeRate(BigInteger address) {
    this.feeRate = address;
  }

  public DaoWithdrawPayload build() {
    return this;
  }
}
