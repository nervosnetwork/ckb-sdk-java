package org.nervos.ckb.transaction;

import java.math.BigInteger;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Sender {

  public String privateKey;
  public BigInteger capacity;

  public Sender(String privateKey, BigInteger capacity) {
    this.privateKey = privateKey;
    this.capacity = capacity;
  }
}
