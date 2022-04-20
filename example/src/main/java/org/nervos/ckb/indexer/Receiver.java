package org.nervos.ckb.indexer;

import java.math.BigInteger;

public class Receiver {
  public String address;
  public long capacity;

  public Receiver(String address, long capacity) {
    this.address = address;
    this.capacity = capacity;
  }
}
