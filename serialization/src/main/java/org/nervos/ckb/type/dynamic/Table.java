package org.nervos.ckb.type.dynamic;

import java.util.Arrays;
import java.util.List;
import org.nervos.ckb.type.base.DynType;
import org.nervos.ckb.type.base.Type;
import org.nervos.ckb.type.fixed.UInt32;

public class Table extends DynType<List<Type>> {

  private List<Type> value;

  public Table(List<Type> value) {
    this.value = value;
  }

  public Table(Type... value) {
    this.value = Arrays.asList(value);
  }

  @Override
  public byte[] toBytes() {
    int length = getLength();
    byte[] dest = new byte[length];
    System.arraycopy(new UInt32(length).toBytes(), 0, dest, 0, UInt32.BYTE_SIZE);

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
  public List<Type> getValue() {
    return value;
  }

  @Override
  public int getLength() {
    int length = 0;
    for (Type type : value) {
      length += type.getLength();
    }
    return length + (1 + value.size()) * UInt32.BYTE_SIZE;
  }
}
