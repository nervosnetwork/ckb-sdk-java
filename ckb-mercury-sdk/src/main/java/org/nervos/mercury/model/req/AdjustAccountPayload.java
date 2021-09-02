package org.nervos.mercury.model.req;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.Set;

public class AdjustAccountPayload {

  @SerializedName("key_address")
  public String address;

  @SerializedName("udt_hashes")
  public Set<String> udtHashes;

  @SerializedName("fee_rate")
  public BigInteger feeRate;

  protected AdjustAccountPayload() {}
}
