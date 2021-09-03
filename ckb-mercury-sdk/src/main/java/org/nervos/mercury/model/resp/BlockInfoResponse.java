package org.nervos.mercury.model.resp;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.List;

/** @author zjh @Created Date: 2021/7/20 @Description: @Modify by: */
public class BlockInfoResponse {
  @SerializedName("block_number")
  public BigInteger blockNumber;

  @SerializedName("block_hash")
  public String blockHash;

  @SerializedName("parent_block_hash")
  public String parentBlockHash;

  public BigInteger timestamp;

  public List<TransactionInfoResponse> transactions;
}
