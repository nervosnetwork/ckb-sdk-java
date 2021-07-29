package org.nervos.mercury.model;

import java.math.BigInteger;
import org.nervos.mercury.FeeConstant;
import org.nervos.mercury.model.req.CollectAssetPayload;
import org.nervos.mercury.model.req.FromAddresses;
import org.nervos.mercury.model.req.ToAddress;

/** @author zjh @Created Date: 2021/7/23 @Description: @Modify by: */
public class CollectAssetPayloadBuilder extends CollectAssetPayload {

  public CollectAssetPayloadBuilder() {
    this.feeRate = FeeConstant.DEFAULT_FEE_RATE;
  }

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
    return this;
  }
}
