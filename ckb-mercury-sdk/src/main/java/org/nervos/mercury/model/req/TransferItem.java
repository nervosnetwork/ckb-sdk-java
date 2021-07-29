package org.nervos.mercury.model.req;

import java.math.BigInteger;

public class TransferItem {

  public ToAddress to;

  public BigInteger amount;

  public TransferItem(ToAddress to, BigInteger amount) {
    this.to = to;
    this.amount = amount;
  }
}
