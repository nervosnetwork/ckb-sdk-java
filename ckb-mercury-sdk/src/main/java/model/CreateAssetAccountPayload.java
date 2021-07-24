package model;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.Set;

public class CreateAssetAccountPayload {

  @SerializedName("key_address")
  public String keyAddress;

  @SerializedName("udt_hashes")
  public Set<String> udtHashes;

  @SerializedName("fee_rate")
  public BigInteger feeRate = new BigInteger("1000");

  public CreateAssetAccountPayload(String keyAddress, Set<String> udtHashes, BigInteger feeRate) {
    this.keyAddress = keyAddress;
    this.udtHashes = udtHashes;
    this.feeRate = feeRate;
  }
}
