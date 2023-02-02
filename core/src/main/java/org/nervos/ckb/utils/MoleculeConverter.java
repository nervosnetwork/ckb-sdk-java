package org.nervos.ckb.utils;

import org.nervos.ckb.type.concrete.*;

import java.math.BigInteger;
import java.util.List;

public class MoleculeConverter {
  public static byte[] toByteArrayLittleEndianUnsigned(BigInteger in, int length) {
    byte[] arr = Numeric.toBytesPadded(in, length);
    byte[] out = new byte[arr.length];
    for (int i = 0; i < arr.length; i++) {
      out[i] = arr[arr.length - i - 1];
    }
    return out;
  }

  public static Uint32 packUint32(int in) {
    byte[] arr = new byte[Integer.BYTES];
    arr[3] = (byte) (in >> Byte.SIZE * 3);
    arr[2] = (byte) (in >> Byte.SIZE * 2);
    arr[1] = (byte) (in >> Byte.SIZE);
    arr[0] = (byte) in;
    return Uint32.builder(arr).build();
  }

  public static Uint64 packUint64(long in) {
    byte[] arr = new byte[Long.BYTES];
    arr[7] = (byte) (in >> Byte.SIZE * 7);
    arr[6] = (byte) (in >> Byte.SIZE * 6);
    arr[5] = (byte) (in >> Byte.SIZE * 5);
    arr[4] = (byte) (in >> Byte.SIZE * 4);
    arr[3] = (byte) (in >> Byte.SIZE * 3);
    arr[2] = (byte) (in >> Byte.SIZE * 2);
    arr[1] = (byte) (in >> Byte.SIZE);
    arr[0] = (byte) in;
    return Uint64.builder(arr).build();
  }

  public static Uint128 packUint128(BigInteger in) {
    byte[] arr = toByteArrayLittleEndianUnsigned(in, Uint128.SIZE);
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

  public static CellInputVec packCellInputVec(List<org.nervos.ckb.type.CellInput> in) {
    CellInput[] arr = new CellInput[in.size()];
    for (int i = 0; i < in.size(); i++) {
      arr[i] = in.get(i).pack();
    }
    return CellInputVec.builder().add(arr).build();
  }

  public static CellOutputVec packCellOutputVec(List<org.nervos.ckb.type.CellOutput> in) {
    CellOutput[] arr = new CellOutput[in.size()];
    for (int i = 0; i < in.size(); i++) {
      arr[i] = in.get(i).pack();
    }
    return CellOutputVec.builder().add(arr).build();
  }

  public static CellDepVec packCellDepVec(List<org.nervos.ckb.type.CellDep> in) {
    CellDep[] arr = new CellDep[in.size()];
    for (int i = 0; i < in.size(); i++) {
      arr[i] = in.get(i).pack();
    }
    return CellDepVec.builder().add(arr).build();
  }

  public static TransactionVec packTransactionVec(List<org.nervos.ckb.type.Transaction> transactions) {
    TransactionVec.Builder builder = TransactionVec.builder();
    Transaction[] packed_transactions = transactions.stream().map(org.nervos.ckb.type.Transaction::pack).toArray(Transaction[]::new);
    builder.add(packed_transactions);
    return builder.build();
  }

  public static ProposalShortId packProposalShortId(byte[] shortId) {
    return ProposalShortId.builder().set(shortId).build();
  }

  public static ProposalShortIdVec packProposalShortIdVec(List<byte[]> shortIds) {
    ProposalShortIdVec.Builder builder = ProposalShortIdVec.builder();
    ProposalShortId[] packed_ids = shortIds.stream().map(MoleculeConverter::packProposalShortId).toArray(ProposalShortId[]::new);
    builder.add(packed_ids);
    return builder.build();
  }

  public static UncleBlock packUncleBlock(org.nervos.ckb.type.Block.Uncle uncle) {
    UncleBlock.Builder builder = UncleBlock.builder();
    builder.setHeader(uncle.header.pack());
    builder.setProposals(packProposalShortIdVec(uncle.proposals));
    return builder.build();
  }

  public static UncleBlockVec packUncleBlockVec(List<org.nervos.ckb.type.Block.Uncle> uncles) {
    UncleBlockVec.Builder builder = UncleBlockVec.builder();
    UncleBlock[] packed_uncles = uncles.stream().map(MoleculeConverter::packUncleBlock).toArray(UncleBlock[]::new);
    builder.add(packed_uncles);
    return builder.build();
  }
}
