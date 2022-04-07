package org.nervos.mercury.model.resp;

import com.google.gson.annotations.SerializedName;

public class TxView<T> {
  public Type type;
  public T value;

  public enum Type {
    @SerializedName("TransactionInfo")
    TRANSACTION_INFO,
    @SerializedName("TransactionWithRichStatus")
    TRANSACTION_WITH_RICH_STATUS
  }
}
