package org.nervos.mercury.model.req;

import java.math.BigInteger;

public class ToInfo {
  public String address;
  public BigInteger amount;

  public ToInfo(String address, BigInteger amount) {
    this.address = address;
    this.amount = amount;
  }
}
