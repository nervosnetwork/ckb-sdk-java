package org.nervos.ckb.transaction;

import org.nervos.ckb.type.CellInput;
import org.nervos.ckb.type.CellOutput;

public class TransactionInput {

  public CellInput input;
  public CellOutput output;

  public TransactionInput(CellInput input, CellOutput output) {
    this.input = input;
    this.output = output;
  }
}
