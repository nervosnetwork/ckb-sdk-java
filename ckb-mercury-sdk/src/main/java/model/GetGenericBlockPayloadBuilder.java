package model;

import java.math.BigInteger;

/** @author zjh @Created Date: 2021/7/20 @Description: @Modify by: */
public class GetGenericBlockPayloadBuilder {
  public BigInteger blockNum;

  public String blockHash;

  public void blockNum(BigInteger blockNum) {
    this.blockNum = blockNum;
  }

  public void blockHash(String blockHash) {
    this.blockHash = blockHash;
  }

  public GetGenericBlockPayload build() {
    return new GetGenericBlockPayload(this.blockNum, this.blockHash);
  }
}
