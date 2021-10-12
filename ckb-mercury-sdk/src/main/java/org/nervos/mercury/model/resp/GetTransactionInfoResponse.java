package org.nervos.mercury.model.resp;

import com.google.gson.annotations.SerializedName;

/** @author zjh @Created Date: 2021/7/20 @Description: @Modify by: */
public class GetTransactionInfoResponse {

  public TransactionInfoResponse transaction;

  public String status;

  @SerializedName("reject_reason")
  public Integer rejectReason;
}
