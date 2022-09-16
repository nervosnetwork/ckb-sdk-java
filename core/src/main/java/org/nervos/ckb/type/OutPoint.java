package org.nervos.ckb.type;

import org.nervos.ckb.utils.Numeric;

import java.util.Arrays;

import static org.nervos.ckb.utils.MoleculeConverter.packByte32;
import static org.nervos.ckb.utils.MoleculeConverter.packUint32;

public class OutPoint {
  public byte[] txHash;

  public int index;

  public OutPoint() {
  }

  public OutPoint(String txHash, int index) {
    this(Numeric.hexStringToByteArray(txHash), index);
  }

  public OutPoint(byte[] txHash, int index) {
    this.txHash = txHash;
    this.index = index;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    OutPoint outPoint = (OutPoint) o;

    if (index != outPoint.index) return false;
    return Arrays.equals(txHash, outPoint.txHash);
  }

  @Override
  public int hashCode() {
    int result = Arrays.hashCode(txHash);
    result = 31 * result + index;
    return result;
  }

  public org.nervos.ckb.type.concrete.OutPoint pack() {
    return org.nervos.ckb.type.concrete.OutPoint.builder()
        .setIndex(packUint32(index))
        .setTxHash(packByte32(txHash))
        .build();
  }
}
