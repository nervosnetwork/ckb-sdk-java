package org.nervos.ckb.type;

import static org.nervos.ckb.utils.MoleculeConverter.packBytes;

public class WitnessArgs {
  private byte[] lock;
  private byte[] inputType;
  private byte[] outputType;

  public byte[] getLock() {
    return lock;
  }

  public void setLock(byte[] lock) {
    this.lock = lock;
  }

  public byte[] getInputType() {
    return inputType;
  }

  public void setInputType(byte[] inputType) {
    this.inputType = inputType;
  }

  public byte[] getOutputType() {
    return outputType;
  }

  public void setOutputType(byte[] outputType) {
    this.outputType = outputType;
  }

  public org.nervos.ckb.newtype.concrete.WitnessArgs pack() {
    return org.nervos.ckb.newtype.concrete.WitnessArgs.builder()
        .setLock(getLock() != null ? packBytes(getLock()) : null)
        .setInputType(getInputType() != null ? packBytes(getInputType()) : null)
        .setOutputType(getOutputType() != null ? packBytes(getOutputType()) : null)
        .build();
  }
}
