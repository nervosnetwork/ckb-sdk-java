package org.nervos.ckb.type;

import org.nervos.ckb.utils.Numeric;

public class PackedTransactionWithStatus {
  public TransactionWithStatus.TxStatus txStatus;
  public String transaction;
  public Long cycles;

  /**
   * @return parsed bytes from transaction string
   */
  public byte[] getTransactionBytes() {
    return Numeric.hexStringToByteArray(this.transaction);
  }
}
