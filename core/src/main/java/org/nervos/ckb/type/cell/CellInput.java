package org.nervos.ckb.type.cell;

import com.google.gson.annotations.SerializedName;
import org.nervos.ckb.type.OutPoint;

import java.math.BigInteger;

import static org.nervos.ckb.utils.MoleculeConverter.packUint64;

public class CellInput {

  @SerializedName("previous_output")
  public OutPoint previousOutput;

  public BigInteger since;

  public CellInput() {}

  public CellInput(OutPoint previousOutput, BigInteger since) {
    this.previousOutput = previousOutput;
    this.since = since;
  }

  public CellInput(OutPoint previousOutput) {
    this(previousOutput, BigInteger.ZERO);
  }

  public org.nervos.ckb.newtype.concrete.CellInput pack() {
    return org.nervos.ckb.newtype.concrete.CellInput.builder()
            .setSince(packUint64(since))
            .setPreviousOutput(previousOutput.pack())
            .build();
  }
}
