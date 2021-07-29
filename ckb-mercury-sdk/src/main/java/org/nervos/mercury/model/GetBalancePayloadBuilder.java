package org.nervos.mercury.model;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashSet;
import org.nervos.mercury.model.req.GetBalancePayload;
import org.nervos.mercury.model.req.KeyAddress;
import org.nervos.mercury.model.req.NormalAddress;

/** @author zjh @Created Date: 2021/7/16 @Description: @Modify by: */
public class GetBalancePayloadBuilder extends GetBalancePayload {

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

  public void address(KeyAddress address) {
    this.address = address;
  }

  public void address(NormalAddress address) {
    this.address = address;
  }

  public void allBalance() {
    this.udtHashes = Collections.EMPTY_SET;
  }

  public GetBalancePayload build() {
    return this;
  }
}
