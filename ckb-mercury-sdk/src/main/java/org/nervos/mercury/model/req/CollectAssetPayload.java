package org.nervos.mercury.model.req;

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

  protected CollectAssetPayload() {}
}
