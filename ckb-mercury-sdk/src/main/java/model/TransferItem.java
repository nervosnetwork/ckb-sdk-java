package model;

import java.math.BigInteger;

public class TransferItem {

  public ToAccount to;

  public BigInteger amount;

  public TransferItem(ToAccount to, BigInteger amount) {
    this.to = to;
    this.amount = amount;
  }
}
