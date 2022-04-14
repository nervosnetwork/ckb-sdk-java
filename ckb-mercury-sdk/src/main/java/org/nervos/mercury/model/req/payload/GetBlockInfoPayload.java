package org.nervos.mercury.model.req.payload;

import com.google.gson.annotations.SerializedName;

/**
 * @author zjh @Created Date: 2021/7/20 @Description: @Modify by:
 */
public class GetBlockInfoPayload {
  @SerializedName("block_number")
  public int blockNumber;

  @SerializedName("block_hash")
  public byte[] blockHash;

  protected GetBlockInfoPayload() {
  }
}
