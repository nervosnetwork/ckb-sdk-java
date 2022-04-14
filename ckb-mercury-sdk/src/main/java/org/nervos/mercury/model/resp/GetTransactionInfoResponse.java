package org.nervos.mercury.model.resp;

import com.google.gson.annotations.SerializedName;
import org.nervos.ckb.type.transaction.TransactionWithStatus;

/**
 * @author zjh @Created Date: 2021/7/20 @Description: @Modify by:
 */
public class GetTransactionInfoResponse {

  public TransactionInfoResponse transaction;

  public TransactionWithStatus.Status status;

  @SerializedName("reject_reason")
  public Integer rejectReason;
}
