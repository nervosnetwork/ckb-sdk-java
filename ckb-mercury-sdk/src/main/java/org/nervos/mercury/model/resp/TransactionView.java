package org.nervos.mercury.model.resp;

import com.google.gson.annotations.SerializedName;
import org.nervos.ckb.type.transaction.TransactionWithStatus;

public class TransactionView {
  @SerializedName("TransactionView")
  public TransactionWithStatus transactionView;
}
