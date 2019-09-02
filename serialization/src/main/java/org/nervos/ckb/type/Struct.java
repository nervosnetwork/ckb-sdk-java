package org.nervos.ckb.type;

import java.util.List;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Struct implements Type<List<Type>> {

  private List<Type> value;

  public Struct(List<Type> value) {
    this.value = value;
  }

  @Override
  public byte[] toBytes() {
    int length = getLength();
    byte[] dest = new byte[length];
    int offset = 0;
    for (Type type : value) {
      System.arraycopy(type.toBytes(), 0, dest, offset, type.getLength());
      offset += type.getLength();
    }
    return dest;
  }

  @Override
  public List<Type> getValue() {
    return value;
  }

  @Override
  public int getLength() {
    int length = 0;
    for (Type type : value) {
      length += type.getLength();
    }
    return length;
  }
}
