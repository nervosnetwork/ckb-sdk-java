package model;

import java.math.BigInteger;

/** @author zjh @Created Date: 2021/7/23 @Description: @Modify by: */
public class CollectAssetPayloadBuilder {

  public String udtHash;

  public FromAddresses fromAddress;

  public ToAddress to;

  public String feePaidBy;

  public BigInteger feeRate = new BigInteger("1000");

  public void udtHash(String udtHash) {
    this.udtHash = udtHash;
  }

  public void fromAddress(FromAddresses fromAddress) {
    this.fromAddress = fromAddress;
  }

  public void to(ToAddress to) {
    this.to = to;
  }

  public void feePaidBy(String feePaidBy) {
    this.feePaidBy = feePaidBy;
  }

  public void feeRate(BigInteger feeRate) {
    this.feeRate = feeRate;
  }

  public CollectAssetPayload build() {
    return new CollectAssetPayload(
        this.udtHash, this.fromAddress, this.to, this.feePaidBy, this.feeRate);
  }
}
