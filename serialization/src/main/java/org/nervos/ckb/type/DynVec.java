package org.nervos.ckb.type;

import java.util.List;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class DynVec implements Type<List<Bytes>> {

  private List<Bytes> value;

  public DynVec(List<Bytes> value) {
    this.value = value;
  }

  @Override
  public byte[] toBytes() {

    int fullLength = getLength();
    byte[] dest = new byte[fullLength];

    // full length bytes
    byte[] lens = new UInt32(fullLength).toBytes();
    System.arraycopy(lens, 0, dest, 0, UInt32.BYTE_SIZE);

    int offset = UInt32.BYTE_SIZE;
    int bytesOffset = UInt32.BYTE_SIZE * (1 + value.size());
    for (Bytes bytes : value) {
      // offset of every Bytes
      byte[] offsetBytes = new UInt32(bytesOffset).toBytes();
      System.arraycopy(offsetBytes, 0, dest, offset, UInt32.BYTE_SIZE);

      // Bytes through offset
      System.arraycopy(bytes.toBytes(), 0, dest, bytesOffset, bytes.getLength());

      offset += UInt32.BYTE_SIZE;
      bytesOffset += bytes.getLength();
    }
    return dest;
  }

  @Override
  public List<Bytes> getValue() {
    return value;
  }

  @Override
  public int getLength() {
    int length = (1 + value.size()) * UInt32.BYTE_SIZE;
    for (Bytes bytes : value) {
      length += bytes.toBytes().length;
    }
    return length;
  }
}
