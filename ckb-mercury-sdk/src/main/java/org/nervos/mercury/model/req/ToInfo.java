package org.nervos.mercury.model.req;

public class ToInfo {
  public String address;
  public long amount;

  public ToInfo(String address, long amount) {
    this.address = address;
    this.amount = amount;
  }
}
