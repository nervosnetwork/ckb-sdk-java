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

  public BigInteger fee;

  public TransferPayload(
      String udtHash, FromAccount from, List<TransferItem> items, String change, BigInteger fee) {
    this.udtHash = udtHash;
    this.from = from;
    this.items = items;
    this.change = change;
    this.fee = fee;
  }
}
