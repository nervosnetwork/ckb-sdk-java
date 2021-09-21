package org.nervos.mercury.model;

import java.math.BigInteger;
import org.nervos.mercury.FeeConstant;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.payload.WithdrawPayload;

public class WithdrawPayloadBuilder extends WithdrawPayload {

  public WithdrawPayloadBuilder() {
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

  public WithdrawPayload build() {
    return this;
  }
}
