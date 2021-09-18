package org.nervos.mercury.model.resp;

import com.google.gson.annotations.SerializedName;
import org.nervos.ckb.type.transaction.TransactionWithStatus;

public class TransactionView implements TxView<TransactionWithStatus> {
  @SerializedName("TransactionView")
  public TransactionWithStatus transactionView;
}
