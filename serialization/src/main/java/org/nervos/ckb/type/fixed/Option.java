package org.nervos.ckb.type.fixed;

import org.nervos.ckb.type.base.FixedType;
import org.nervos.ckb.type.base.Type;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Option extends FixedType<Type> {

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
