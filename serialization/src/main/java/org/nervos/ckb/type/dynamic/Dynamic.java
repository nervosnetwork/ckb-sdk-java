package org.nervos.ckb.type.dynamic;

import java.util.List;
import org.nervos.ckb.type.base.Type;
import org.nervos.ckb.type.fixed.UInt32;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Dynamic<T extends Type> implements Type<List<T>> {
  private List<T> value;

  public Dynamic(List<T> value) {
    this.value = value;
  }

  @Override
  public byte[] toBytes() {

    int fullLength = getLength();
    byte[] dest = new byte[fullLength];

    // full length bytes
    System.arraycopy(new UInt32(fullLength).toBytes(), 0, dest, 0, UInt32.BYTE_SIZE);

    int offset = UInt32.BYTE_SIZE;
    int bytesOffset = UInt32.BYTE_SIZE * (1 + value.size());

    for (Type type : value) {
      // offset of every Bytes
      byte[] offsetBytes = new UInt32(bytesOffset).toBytes();
      System.arraycopy(offsetBytes, 0, dest, offset, UInt32.BYTE_SIZE);

      // Bytes through offset
      System.arraycopy(type.toBytes(), 0, dest, bytesOffset, type.getLength());

      offset += UInt32.BYTE_SIZE;
      bytesOffset += type.getLength();
    }
    return dest;
  }

  @Override
  public List<T> getValue() {
    return value;
  }

  @Override
  public int getLength() {
    int length = (1 + value.size()) * UInt32.BYTE_SIZE;
    for (Type type : value) {
      length += type.getLength();
    }
    return length;
  }
}
