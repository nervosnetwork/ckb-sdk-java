package org.nervos.ckb.transaction;

import org.nervos.ckb.type.CellInput;

public class TransactionInput {

  public CellInput input;
  public long capacity;
  public byte[] lockHash;

  public TransactionInput(CellInput input, long capacity, byte[] lockHash) {
    this.input = input;
    this.capacity = capacity;
    this.lockHash = lockHash;
  }
}
