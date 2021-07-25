package model;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;

/** @author zjh @Created Date: 2021/7/23 @Description: @Modify by: */
public class CollectAssetPayload {
  @SerializedName("udt_hash")
  public String udtHash;

  @SerializedName("from_address")
  public FromAddresses fromAddress;

  public ToAddress to;

  @SerializedName("fee_paid_by")
  public String feePaidBy;

  @SerializedName("fee_rate")
  public BigInteger feeRate;

  public CollectAssetPayload(
      String udtHash,
      FromAddresses fromAddress,
      ToAddress to,
      String feePaidBy,
      BigInteger feeRate) {
    this.udtHash = udtHash;
    this.fromAddress = fromAddress;
    this.to = to;
    this.feePaidBy = feePaidBy;
    this.feeRate = feeRate;
  }
}
