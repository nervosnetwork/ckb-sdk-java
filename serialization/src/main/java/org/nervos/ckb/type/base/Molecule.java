package org.nervos.ckb.type.base;

public abstract class Molecule {
  protected byte[] buf;

  public byte[] toByteArray() {
    return buf;
  }

  public int getSize() {
    return toByteArray().length;
  }
}
