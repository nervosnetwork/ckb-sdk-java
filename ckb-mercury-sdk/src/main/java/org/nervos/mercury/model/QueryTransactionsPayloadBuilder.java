package org.nervos.mercury.model;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashSet;
import org.nervos.mercury.model.req.KeyAddress;
import org.nervos.mercury.model.req.NormalAddress;
import org.nervos.mercury.model.req.QueryTransactionsPayload;

/** @author zjh @Created Date: 2021/7/26 @Description: @Modify by: */
public class QueryTransactionsPayloadBuilder extends QueryTransactionsPayload {

  public QueryTransactionsPayloadBuilder() {
    this.udtHashes = new HashSet<>(2, 1);
    this.udtHashes.add(null);
  }

  public void address(KeyAddress address) {
    this.address = address;
  }

  public void address(NormalAddress address) {
    this.address = address;
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

  public QueryTransactionsPayload build() {
    return this;
  }
}
