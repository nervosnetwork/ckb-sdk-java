package org.nervos.mercury.model.resp;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.type.transaction.TransactionWithStatus;

public class TransactionWithRichStatus {

  public Transaction transaction;

  @SerializedName("tx_status")
  public TxRichStatus txStatus;

  public class TxRichStatus {
    public TransactionWithStatus.Status status;

    @SerializedName("block_hash")
    public byte[] blockHash;

    public String reason;
    public long timestamp;
  }
}
