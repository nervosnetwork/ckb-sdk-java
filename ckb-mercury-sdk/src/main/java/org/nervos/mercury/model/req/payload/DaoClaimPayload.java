package org.nervos.mercury.model.req.payload;

import org.nervos.mercury.model.req.item.Item;

import java.math.BigInteger;

public class DaoClaimPayload {
  public Item from;
  public String to;
  public BigInteger feeRate;

  protected DaoClaimPayload() {
  }
}
