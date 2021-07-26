package model;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;

/** @author zjh @Created Date: 2021/7/20 @Description: @Modify by: */
public class GetGenericBlockPayload {
  @SerializedName("block_num")
  public BigInteger blockNum;

  @SerializedName("block_hash")
  public String blockHash;

  protected GetGenericBlockPayload(BigInteger blockNum, String blockHash) {
    this.blockNum = blockNum;
    this.blockHash = blockHash;
  }
}
