package org.nervos.mercury.model.resp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/** @author zjh @Created Date: 2021/7/20 @Description: @Modify by: */
public class BlockInfoResponse {
  @SerializedName("block_number")
  public int blockNumber;

  @SerializedName("block_hash")
  public byte[] blockHash;

  @SerializedName("parent_hash")
  public byte[] parentHash;

  public long timestamp;

  public List<TransactionInfoResponse> transactions;
}
