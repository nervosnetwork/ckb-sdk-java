package org.nervos.mercury.model.req;

import java.math.BigInteger;

public class SmartTo {
  public String address;
  public BigInteger amount;

  public SmartTo(String address, BigInteger amount) {
    this.address = address;
    this.amount = amount;
  }
}
