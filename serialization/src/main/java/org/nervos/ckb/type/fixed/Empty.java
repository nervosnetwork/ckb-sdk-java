package org.nervos.ckb.type.fixed;

import org.nervos.ckb.type.base.FixedType;

public class Empty extends FixedType {
  @Override
  public byte[] toBytes() {
    return new byte[0];
  }

  @Override
  public Object getValue() {
    return null;
  }

  @Override
  public int getLength() {
    return 0;
  }
}
