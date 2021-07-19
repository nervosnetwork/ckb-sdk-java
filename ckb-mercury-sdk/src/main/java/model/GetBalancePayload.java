package model;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.HashSet;

/** @author zjh @Created Date: 2021/7/16 @Description: @Modify by: */
public class GetBalancePayload {
  @SerializedName("udt_hashes")
  public HashSet<String> udtHashes;

  @SerializedName("block_num")
  public BigInteger blockNum;

  public QueryAddress address;

  public GetBalancePayload(HashSet<String> udtHashes, BigInteger blockNum, QueryAddress address) {
    this.udtHashes = udtHashes;
    this.blockNum = blockNum;
    this.address = address;
  }
}
