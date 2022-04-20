package org.nervos.ckb.transaction;

import org.nervos.ckb.type.cell.CellInput;

public class TransactionInput {

  public CellInput input;
  public long capacity;
  public String lockHash;

  public TransactionInput(CellInput input, long capacity, String lockHash) {
    this.input = input;
    this.capacity = capacity;
    this.lockHash = lockHash;
  }
}
