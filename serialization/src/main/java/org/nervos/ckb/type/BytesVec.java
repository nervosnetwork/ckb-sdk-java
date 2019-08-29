package org.nervos.ckb.type;

import java.util.ArrayList;
import java.util.List;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class BytesVec implements Type<List<Bytes>> {

  private List<Bytes> value;

  public BytesVec(List<Bytes> value) {
    this.value = value;
  }

  public static BytesVec fromBytes(byte[] bytes) {
    byte[] lens = new byte[Int.HEX_SIZE];
    System.arraycopy(bytes, 0, lens, 0, Int.HEX_SIZE);

    int fullLen = new Int(lens).getValue();
    int length = (bytes.length - fullLen) / Int.HEX_SIZE;
    List<Bytes> value = new ArrayList<>(length);

    int offset = Int.HEX_SIZE;
    byte[] indexBytesLens = new byte[Int.HEX_SIZE];
    for (int i = 0; i < length; i++) {
      offset += Int.HEX_SIZE;
      System.arraycopy(bytes, offset, indexBytesLens, 0, Int.HEX_SIZE);
      int indexBytesLength = new Int(indexBytesLens).getValue();
      byte[] indexBytes = new byte[indexBytesLength];
      offset += Int.HEX_SIZE;
      System.arraycopy(bytes, offset, indexBytes, 0, indexBytesLength);
      value.add(Bytes.fromBytes(indexBytes));
      offset += indexBytesLength;
    }
    return new BytesVec(value);
  }

  @Override
  public byte[] toBytes() {
    int destLength = (1 + value.size()) * Int.HEX_SIZE;
    for (Bytes bytes : value) {
      destLength += bytes.toBytes().length;
    }

    byte[] dest = new byte[destLength];
    byte[] lens = new Int(value.size()).toBytes();
    System.arraycopy(lens, 0, dest, 0, Int.HEX_SIZE);
    int offset = Int.HEX_SIZE;
    for (Bytes bytes : value) {
      System.arraycopy(new Int(offset).toBytes(), 0, dest, offset, Int.HEX_SIZE);
      offset += Int.HEX_SIZE;
      System.arraycopy(bytes.toBytes(), bytes.toBytes().length, dest, offset, Int.HEX_SIZE);
      offset += bytes.toBytes().length;
    }
    return dest;
  }

  @Override
  public List<Bytes> getValue() {
    return value;
  }
}
