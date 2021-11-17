package org.nervos.mercury.model.resp;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import org.nervos.ckb.type.transaction.Transaction;

public class TransactionWithRichStatus {

  public Transaction transaction;

  @SerializedName("tx_status")
  public TxRichStatus txStatus;

  public class TxRichStatus {
    public String status;

    @SerializedName("block_hash")
    public String blockHash;

    public String reason;
    public BigInteger timestamp;
  }
}
