package org.nervos.mercury.model.req;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;

/** @author zjh @Created Date: 2021/7/20 @Description: @Modify by: */
public class GetBlockInfoPayload {
  @SerializedName("block_number")
  public BigInteger blockNumber;

  @SerializedName("block_hash")
  public String blockHash;

  protected GetBlockInfoPayload() {}
}
