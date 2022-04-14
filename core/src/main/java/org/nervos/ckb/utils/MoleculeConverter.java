package org.nervos.ckb.utils;

import java.math.BigInteger;
import java.util.List;
import org.nervos.ckb.newtype.concrete.*;

public class MoleculeConverter {
  public static byte[] littleEndianBigInteger(BigInteger in, int length) {
    byte[] arr = Numeric.toBytesPadded(in, length);
    byte[] out = new byte[arr.length];
    for (int i = 0; i < arr.length; i++) {
      out[i] = arr[arr.length - i - 1];
    }
    return out;
  }

  // TODO: will be removed after the new type to Uint32 is ready
  public static Uint32 packUint32(long in) {
    return packUint32(BigInteger.valueOf(in));
  }

  // TODO: will be removed after the new type to Uint32 is ready
  public static Uint32 packUint32(BigInteger in) {
    byte[] arr = littleEndianBigInteger(in, Uint32.SIZE);
    return Uint32.builder(arr).build();
  }

  // TODO: will be removed after the new type to Uint64 is ready
  public static Uint64 packUint64(BigInteger in) {
    byte[] arr = littleEndianBigInteger(in, Uint64.SIZE);
    return Uint64.builder(arr).build();
  }

  public static Uint64 packUint64(long in) {
    return packUint64(BigInteger.valueOf(in));
  }

  public static Uint128 packUint128(BigInteger in) {
    byte[] arr = littleEndianBigInteger(in, Uint128.SIZE);
    return Uint128.builder(arr).build();
  }

  public static Byte32 packByte32(byte[] in) {
    return Byte32.builder(in).build();
  }

  public static Bytes packBytes(byte[] in) {
    if (in == null) {
      return null;
    }
    return Bytes.builder().add(in).build();
  }

  public static BytesVec packBytesVec(List<byte[]> in) {
    Bytes[] arr = new Bytes[in.size()];
    for (int i = 0; i < in.size(); i++) {
      arr[i] = packBytes(in.get(i));
    }
    return BytesVec.builder().add(arr).build();
  }

  public static Byte32Vec packByte32Vec(List<byte[]> in) {
    Byte32[] arr = new Byte32[in.size()];
    for (int i = 0; i < in.size(); i++) {
      arr[i] = packByte32(in.get(i));
    }
    return Byte32Vec.builder().add(arr).build();
  }

  public static CellInputVec packCellInputVec(List<org.nervos.ckb.type.cell.CellInput> in) {
    CellInput[] arr = new CellInput[in.size()];
    for (int i = 0; i < in.size(); i++) {
      arr[i] = in.get(i).pack();
    }
    return CellInputVec.builder().add(arr).build();
  }

  public static CellOutputVec packCellOutputVec(List<org.nervos.ckb.type.cell.CellOutput> in) {
    CellOutput[] arr = new CellOutput[in.size()];
    for (int i = 0; i < in.size(); i++) {
      arr[i] = in.get(i).pack();
    }
    return CellOutputVec.builder().add(arr).build();
  }

  public static CellDepVec packCellDepVec(List<org.nervos.ckb.type.cell.CellDep> in) {
    CellDep[] arr = new CellDep[in.size()];
    for (int i = 0; i < in.size(); i++) {
      arr[i] = in.get(i).pack();
    }
    return CellDepVec.builder().add(arr).build();
  }
}
