package org.nervos.ckb.type;

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
