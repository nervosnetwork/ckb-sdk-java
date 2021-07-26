package model;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/** @author zjh @Created Date: 2021/7/26 @Description: @Modify by: */
public class QueryGenericTransactionsPayloadBuilder {

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

  public QueryGenericTransactionsPayloadBuilder() {
    this.udtHashes = new HashSet<>(2, 1);
    this.udtHashes.add(null);
  }

  public void address(String address) {
    this.address = QueryAddress.getQueryAddressByAddress(address);
  }

  public void addUdtHash(String udtHash) {
    if (this.udtHashes.contains(null)) {
      this.udtHashes.remove(null);
    }
    this.udtHashes.add(udtHash);
  }

  public void allTransactionType() {
    this.udtHashes = Collections.EMPTY_SET;
  }

  public void fromBlock(BigInteger fromBlock) {
    this.fromBlock = fromBlock;
  }

  public void toBlock(BigInteger toBlock) {
    this.toBlock = toBlock;
  }

  public void limit(BigInteger limit) {
    this.limit = limit;
  }

  public void offset(BigInteger offset) {
    this.offset = offset;
  }

  public void order(String order) {
    this.order = order;
  }

  public QueryGenericTransactionsPayload build() {
    return new QueryGenericTransactionsPayload(
        this.address,
        this.udtHashes,
        this.fromBlock,
        this.toBlock,
        this.limit,
        this.offset,
        this.order);
  }
}
