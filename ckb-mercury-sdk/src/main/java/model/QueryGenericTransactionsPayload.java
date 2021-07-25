package model;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.Set;

/** @author zjh @Created Date: 2021/7/26 @Description: @Modify by: */
public class QueryGenericTransactionsPayload {

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

  public QueryGenericTransactionsPayload(
      QueryAddress address,
      Set<String> udtHashes,
      BigInteger fromBlock,
      BigInteger toBlock,
      BigInteger limit,
      BigInteger offset,
      String order) {
    this.address = address;
    this.udtHashes = udtHashes;
    this.fromBlock = fromBlock;
    this.toBlock = toBlock;
    this.limit = limit;
    this.offset = offset;
    this.order = order;
  }
}
