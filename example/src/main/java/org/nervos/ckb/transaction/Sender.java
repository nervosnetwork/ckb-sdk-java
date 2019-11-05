package org.nervos.ckb.transaction;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Sender {

  public List<String> privateKeys;
  public String multiSigHash;
  public BigInteger capacity;

  public Sender(String privateKey, BigInteger capacity) {
    this.privateKeys = Collections.singletonList(privateKey);
    this.capacity = capacity;
  }

  public Sender(List<String> privateKeys, BigInteger capacity) {
    this.privateKeys = privateKeys;
    this.capacity = capacity;
  }

  public Sender(List<String> privateKeys, String multiSigHash, BigInteger capacity) {
    this.privateKeys = privateKeys;
    this.multiSigHash = multiSigHash;
    this.capacity = capacity;
  }
}
