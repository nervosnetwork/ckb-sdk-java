package org.nervos.mercury.model.req;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.List;

public class TransferPayload {

  @SerializedName("udt_hash")
  public String udtHash;

  public FromAddresses from;

  public List<TransferItem> items;

  public String change;

  @SerializedName("fee_rate")
  public BigInteger feeRate = new BigInteger("1000");

  protected TransferPayload() {}
}
