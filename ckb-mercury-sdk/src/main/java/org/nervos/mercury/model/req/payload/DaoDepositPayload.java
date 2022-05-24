package org.nervos.mercury.model.req.payload;

import org.nervos.mercury.model.req.From;

public class DaoDepositPayload {
  public From from;
  public String to;
  public long amount;
  public Long feeRate;

  protected DaoDepositPayload() {
  }
}
