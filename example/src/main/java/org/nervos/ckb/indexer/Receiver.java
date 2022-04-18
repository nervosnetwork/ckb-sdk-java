package org.nervos.ckb.indexer;

import java.math.BigInteger;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Receiver {
  public String address;
  public long capacity;

  public Receiver(String address, long capacity) {
    this.address = address;
    this.capacity = capacity;
  }
}
