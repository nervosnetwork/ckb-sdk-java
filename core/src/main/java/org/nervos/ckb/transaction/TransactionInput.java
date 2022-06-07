package org.nervos.ckb.transaction;

import org.nervos.ckb.type.CellInput;
import org.nervos.ckb.type.CellOutput;

public class TransactionInput {

  public CellInput input;
  public CellOutput output;
  public byte[] outputData;

  public TransactionInput(CellInput input, CellOutput output) {
    this(input, output, new byte[0]);
  }

  public TransactionInput(CellInput input, CellOutput output, byte[] outputData) {
    this.input = input;
    this.output = output;
    this.outputData = outputData;
  }
}
