package org.nervos.ckb.transaction;

import java.math.BigInteger;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Receiver {
  public String address;
  public BigInteger capacity;

  public Receiver(String address, BigInteger capacity) {
    this.address = address;
    this.capacity = capacity;
  }
}
