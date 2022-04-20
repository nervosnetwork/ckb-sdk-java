package org.nervos.ckb.type.fixed;

import org.nervos.ckb.type.base.FixedType;
import org.nervos.ckb.type.base.Type;

import java.util.List;

public class Fixed<T extends FixedType> implements Type<List<T>> {

  private List<T> value;

  public Fixed(List<T> value) {
    this.value = value;
  }

  @Override
  public byte[] toBytes() {

    int fullLength = getLength();
    byte[] dest = new byte[fullLength];

    // full length bytes
    System.arraycopy(new UInt32(value.size()).toBytes(), 0, dest, 0, UInt32.BYTE_SIZE);

    int offset = UInt32.BYTE_SIZE;
    for (FixedType type : value) {
      // Bytes through offset
      System.arraycopy(type.toBytes(), 0, dest, offset, type.getLength());
      offset += type.getLength();
    }
    return dest;
  }

  @Override
  public List<T> getValue() {
    return value;
  }

  @Override
  public int getLength() {
    int length = UInt32.BYTE_SIZE;
    for (FixedType type : value) {
      length += type.getLength();
    }
    return length;
  }
}
