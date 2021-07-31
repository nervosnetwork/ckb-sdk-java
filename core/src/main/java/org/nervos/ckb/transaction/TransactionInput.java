package org.nervos.ckb.transaction;

import java.math.BigInteger;
import org.nervos.ckb.type.cell.CellInput;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class TransactionInput {

  public CellInput input;
  public BigInteger capacity;
  public String lockHash;

  public TransactionInput(CellInput input, BigInteger capacity, String lockHash) {
    this.input = input;
    this.capacity = capacity;
    this.lockHash = lockHash;
  }
}
