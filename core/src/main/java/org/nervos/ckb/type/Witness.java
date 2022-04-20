package org.nervos.ckb.type;

import org.nervos.ckb.utils.Numeric;

// The Witness class here corresponds to the WitnessArgs of CKB
public class Witness {

  public static final byte[] SIGNATURE_PLACEHOLDER =
      Numeric.hexStringToByteArray(
          "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");

  public byte[] lock;
  public byte[] inputType;
  public byte[] outputType;

  public Witness() {
    this.lock = new byte[]{};
    this.inputType = new byte[]{};
    this.outputType = new byte[]{};
  }

  public Witness(byte[] lock) {
    this.lock = lock;
    this.inputType = new byte[]{};
    this.outputType = new byte[]{};
  }

  public Witness(byte[] lock, byte[] inputType, byte[] outputType) {
    this.lock = lock;
    this.inputType = inputType;
    this.outputType = outputType;
  }
}
