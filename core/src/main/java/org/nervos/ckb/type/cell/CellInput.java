package org.nervos.ckb.type.cell;

import static org.nervos.ckb.utils.MoleculeConverter.packUint64;

import org.nervos.ckb.type.OutPoint;

public class CellInput {
  public OutPoint previousOutput;
  public long since;

  public CellInput() {}

  public CellInput(OutPoint previousOutput, long since) {
    this.previousOutput = previousOutput;
    this.since = since;
  }

  public CellInput(OutPoint previousOutput) {
    this(previousOutput, 0);
  }

  public org.nervos.ckb.newtype.concrete.CellInput pack() {
    return org.nervos.ckb.newtype.concrete.CellInput.builder()
        .setSince(packUint64(since))
        .setPreviousOutput(previousOutput.pack())
        .build();
  }
}
