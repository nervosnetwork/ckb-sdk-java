package org.nervos.mercury.model.resp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/** @author zjh @Created Date: 2021/7/20 @Description: @Modify by: */
public class TransactionInfoResponse {
  @SerializedName("tx_hash")
  public String txHash;

  @SerializedName("operations")
  public List<RecordResponse> records;
}
