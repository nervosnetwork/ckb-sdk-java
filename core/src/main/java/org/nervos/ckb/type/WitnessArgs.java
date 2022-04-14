package org.nervos.ckb.type;

import static org.nervos.ckb.utils.MoleculeConverter.packBytes;

import java.util.Arrays;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    WitnessArgs that = (WitnessArgs) o;

    if (!Arrays.equals(lock, that.lock)) return false;
    if (!Arrays.equals(inputType, that.inputType)) return false;
    return Arrays.equals(outputType, that.outputType);
  }

  @Override
  public int hashCode() {
    int result = Arrays.hashCode(lock);
    result = 31 * result + Arrays.hashCode(inputType);
    result = 31 * result + Arrays.hashCode(outputType);
    return result;
  }

  public org.nervos.ckb.newtype.concrete.WitnessArgs pack() {
    return org.nervos.ckb.newtype.concrete.WitnessArgs.builder()
        .setLock(getLock() != null ? packBytes(getLock()) : null)
        .setInputType(getInputType() != null ? packBytes(getInputType()) : null)
        .setOutputType(getOutputType() != null ? packBytes(getOutputType()) : null)
        .build();
  }

  public static WitnessArgs unpack(byte[] in) {
    org.nervos.ckb.newtype.concrete.WitnessArgs moleculeWitnessArgs =
        org.nervos.ckb.newtype.concrete.WitnessArgs.builder(in).build();

    WitnessArgs witnessArgs = new WitnessArgs();
    witnessArgs.setLock(
        moleculeWitnessArgs.getLock() != null ? moleculeWitnessArgs.getLock().getItems() : null);
    witnessArgs.setInputType(
        moleculeWitnessArgs.getInputType() != null
            ? moleculeWitnessArgs.getInputType().getItems()
            : null);
    witnessArgs.setOutputType(
        moleculeWitnessArgs.getOutputType() != null
            ? moleculeWitnessArgs.getOutputType().getItems()
            : null);

    return witnessArgs;
  }
}
