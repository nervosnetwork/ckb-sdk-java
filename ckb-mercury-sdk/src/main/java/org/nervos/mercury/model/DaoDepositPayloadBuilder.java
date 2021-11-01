package org.nervos.mercury.model;

import java.math.BigInteger;
import org.nervos.mercury.FeeConstant;
import org.nervos.mercury.model.req.From;
import org.nervos.mercury.model.req.payload.DaoDepositPayload;

public class DaoDepositPayloadBuilder extends DaoDepositPayload {
  public DaoDepositPayloadBuilder() {
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

  public DaoDepositPayload build() {
    return this;
  }
}
