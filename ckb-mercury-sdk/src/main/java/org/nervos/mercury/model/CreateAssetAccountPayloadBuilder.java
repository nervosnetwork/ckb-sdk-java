package org.nervos.mercury.model;

import java.math.BigInteger;
import java.util.HashSet;
import org.nervos.mercury.FeeConstant;
import org.nervos.mercury.model.req.CreateAssetAccountPayload;

public class CreateAssetAccountPayloadBuilder extends CreateAssetAccountPayload {

  public CreateAssetAccountPayloadBuilder() {
    this.feeRate = FeeConstant.DEFAULT_FEE_RATE;
    this.udtHashes = new HashSet<>(2, 1);
  }

  public void keyAddress(String keyAddress) {
    this.keyAddress = keyAddress;
  }

  public void addUdtHash(String udtHash) {
    this.udtHashes.add(udtHash);
  }

  public void feeRate(BigInteger feeRate) {
    this.feeRate = feeRate;
  }

  public CreateAssetAccountPayload build() {
    return this;
  }
}
