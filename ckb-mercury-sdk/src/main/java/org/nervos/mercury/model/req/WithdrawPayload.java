package org.nervos.mercury.model.req;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import org.nervos.mercury.model.req.item.Item;

public class WithdrawPayload {
  public Item from;

  @SerializedName("pay_fee")
  public Item payFee;

  @SerializedName("fee_rate")
  public BigInteger feeRate;

  protected WithdrawPayload() {}
}
