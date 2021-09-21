package org.nervos.mercury.model.resp;

import com.google.gson.annotations.SerializedName;

public class TransactionInfo implements TxView<TransactionInfoResponse> {
  @SerializedName("TransactionInfo")
  public TransactionInfoResponse transactionInfo;
}
