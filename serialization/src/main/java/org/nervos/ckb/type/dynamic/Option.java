package org.nervos.ckb.type.dynamic;

import org.nervos.ckb.type.base.DynType;
import org.nervos.ckb.type.base.Type;

public class Option extends DynType<Type> {

  private Type value;

  public Option(Type value) {
    this.value = value;
  }

  @Override
  public int getLength() {
    return value == null ? 0 : value.getLength();
  }

  @Override
  public Type getValue() {
    return value;
  }

  @Override
  public byte[] toBytes() {
    return value == null ? new byte[0] : value.toBytes();
  }
}
