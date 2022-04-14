package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

import static org.nervos.ckb.utils.MoleculeConverter.packByte32;
import static org.nervos.ckb.utils.MoleculeConverter.packUint32;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class OutPoint {

  @SerializedName("tx_hash")
  public byte[] txHash;

  public int index;

  public OutPoint() {}

  public OutPoint(byte[] txHash, int index) {
    this.txHash = txHash;
    this.index = index;
  }

  public org.nervos.ckb.newtype.concrete.OutPoint pack() {
    return org.nervos.ckb.newtype.concrete.OutPoint.builder()
            .setIndex(packUint32(index))
            .setTxHash(packByte32(txHash))
            .build();
  }
}
