package org.nervos.mercury.model.req;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.Set;

/** @author zjh @Created Date: 2021/7/26 @Description: @Modify by: */
public class QueryTransactionsPayload {

  public QueryAddress address;

  @SerializedName("udt_hashes")
  public Set<String> udtHashes;

  @SerializedName("from_block")
  public BigInteger fromBlock;

  @SerializedName("to_block")
  public BigInteger toBlock;

  public BigInteger limit;

  public BigInteger offset;

  public String order;

  protected QueryTransactionsPayload() {}
}
