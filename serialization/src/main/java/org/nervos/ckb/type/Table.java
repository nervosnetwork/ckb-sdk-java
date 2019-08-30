package org.nervos.ckb.type;

import java.util.List;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Table implements Type<List<Type>> {

  private List<Type> value;

  public Table(List<Type> value) {
    this.value = value;
  }

  @Override
  public byte[] toBytes() {
    int length = getLength();
    byte[] dest = new byte[length];
    System.arraycopy(new Int(length).toBytes(), 0, dest, 0, Int.BYTE_SIZE);

    int offset = Int.BYTE_SIZE * (1 + value.size());
    for (int i = 0; i < value.size(); i++) {
      System.arraycopy(new Int(offset).toBytes(), 0, dest, Int.BYTE_SIZE * (1 + i), Int.BYTE_SIZE);
      System.arraycopy(value.get(i).toBytes(), 0, dest, offset, value.get(i).getLength());
      offset += value.get(i).getLength();
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
    return length + (1 + value.size()) * Int.BYTE_SIZE;
  }
}
