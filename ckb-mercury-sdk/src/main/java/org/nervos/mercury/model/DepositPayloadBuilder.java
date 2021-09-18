package org.nervos.mercury.model;

import java.math.BigInteger;
import org.nervos.mercury.FeeConstant;
import org.nervos.mercury.model.req.From;
import org.nervos.mercury.model.req.payload.DepositPayload;

public class DepositPayloadBuilder extends DepositPayload {
  public DepositPayloadBuilder() {
    this.feeRate = FeeConstant.DEFAULT_FEE_RATE;
  }

  public void from(From from) {
    this.from = from;
  }

  public void to(String to) {
    this.to = to;
  }

  public void amount(BigInteger amount) {
    this.amount = amount;
  }

  public void feeRate(BigInteger feeRate) {
    this.feeRate = feeRate;
  }

  public DepositPayload build() {
    return this;
  }
}
