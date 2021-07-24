package model;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public class CreateAssetAccountPayloadBuilder {

  public String keyAddress;

  public Set<String> udtHashes = new HashSet<>(2, 1);

  public BigInteger feeRate = new BigInteger("1000");

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
    return new CreateAssetAccountPayload(keyAddress, udtHashes, feeRate);
  }
}
