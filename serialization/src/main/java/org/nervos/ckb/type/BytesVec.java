package org.nervos.ckb.type;

import java.util.List;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class BytesVec implements Type<List<Bytes>> {

  private List<Bytes> value;

  public BytesVec(List<Bytes> value) {
    this.value = value;
  }

  @Override
  public byte[] toBytes() {

    int fullLength = getLength();
    byte[] dest = new byte[fullLength];

    // full length bytes
    byte[] lens = new Int(fullLength).toBytes();
    System.arraycopy(lens, 0, dest, 0, Int.BYTE_SIZE);

    int offset = Int.BYTE_SIZE;
    int bytesOffset = Int.BYTE_SIZE * (1 + value.size());
    for (Bytes bytes : value) {
      // offset of every Bytes
      byte[] offsetBytes = new Int(bytesOffset).toBytes();
      System.arraycopy(offsetBytes, 0, dest, offset, Int.BYTE_SIZE);

      // Bytes through offset
      System.arraycopy(bytes.toBytes(), 0, dest, bytesOffset, bytes.getLength());

      offset += Int.BYTE_SIZE;
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
    int length = (1 + value.size()) * Int.BYTE_SIZE;
    for (Bytes bytes : value) {
      length += bytes.toBytes().length;
    }
    return length;
  }
}
