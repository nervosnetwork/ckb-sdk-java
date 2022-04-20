package org.nervos.mercury.model.req.payload;

import org.nervos.mercury.model.req.item.Item;

import java.math.BigInteger;

public class DaoWithdrawPayload {
  public Item from;
  public String payFee;
  public BigInteger feeRate;

  protected DaoWithdrawPayload() {
  }
}
