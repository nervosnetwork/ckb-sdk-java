package org.nervos.mercury.model.req.payload;

import org.nervos.mercury.model.req.From;

import java.math.BigInteger;

public class DaoDepositPayload {
  public From from;
  public String to;
  public long amount;
  public BigInteger feeRate;

  protected DaoDepositPayload() {
  }
}
