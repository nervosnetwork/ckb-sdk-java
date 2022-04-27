package org.nervos.ckb.type;

import static org.nervos.ckb.utils.MoleculeConverter.packByte32;
import static org.nervos.ckb.utils.MoleculeConverter.packUint32;

public class OutPoint {
  public byte[] txHash;

  public int index;

  public OutPoint() {
  }

  public OutPoint(byte[] txHash, int index) {
    this.txHash = txHash;
    this.index = index;
  }

  public org.nervos.ckb.type.concrete.OutPoint pack() {
    return org.nervos.ckb.type.concrete.OutPoint.builder()
        .setIndex(packUint32(index))
        .setTxHash(packByte32(txHash))
        .build();
  }
}
