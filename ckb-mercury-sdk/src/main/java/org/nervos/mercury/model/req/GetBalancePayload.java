package org.nervos.mercury.model.req;

import com.google.gson.annotations.SerializedName;
import java.util.Set;

/** @author zjh @Created Date: 2021/7/16 @Description: @Modify by: */
public class GetBalancePayload {
  @SerializedName("udt_hashes")
  public Set<String> udtHashes;

  //  @SerializedName("block_num")
  //  public BigInteger blockNum;

  public QueryAddress address;

  protected GetBalancePayload() {}
}
