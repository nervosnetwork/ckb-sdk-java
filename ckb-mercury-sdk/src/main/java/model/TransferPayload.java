package model;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.List;

public class TransferPayload {

  @SerializedName("udt_hash")
  public String udtHash;

  public FromAccount from;

  public List<TransferItem> items;

  public String change;

  @SerializedName("fee_rate")
  public BigInteger feeRate = new BigInteger("1000");

  public TransferPayload(
      String udtHash,
      FromAccount from,
      List<TransferItem> items,
      String change,
      BigInteger feeRate) {
    this.udtHash = udtHash;
    this.from = from;
    this.items = items;
    this.change = change;
    this.feeRate = feeRate;
  }
}
