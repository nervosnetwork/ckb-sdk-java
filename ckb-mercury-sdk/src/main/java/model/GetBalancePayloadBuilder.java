package model;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/** @author zjh @Created Date: 2021/7/16 @Description: @Modify by: */
public class GetBalancePayloadBuilder {
  public Set<String> udtHashes;

  public BigInteger blockNum;

  public QueryAddress address;

  public GetBalancePayloadBuilder() {
    this.udtHashes = new HashSet<>(2, 1);
    this.udtHashes.add(null);
  }

  public void addUdtHash(String udtHash) {
    if (this.udtHashes.contains(null)) {
      this.udtHashes.remove(null);
    }
    this.udtHashes.add(udtHash);
  }

  public void blockNum(BigInteger blockNum) {
    this.blockNum = blockNum;
  }

  public void address(String address) {
    this.address = QueryAddress.getQueryAddressByAddress(address);
  }

  public void allBalance() {
    this.udtHashes = Collections.EMPTY_SET;
  }

  public GetBalancePayload build() {
    return new GetBalancePayload(this.udtHashes, this.blockNum, this.address);
  }
}
