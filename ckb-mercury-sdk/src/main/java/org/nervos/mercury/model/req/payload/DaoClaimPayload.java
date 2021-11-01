package org.nervos.mercury.model.req.payload;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import org.nervos.mercury.model.req.item.Item;

public class DaoClaimPayload {
  public Item from;

  public String to;

  @SerializedName("fee_rate")
  public BigInteger feeRate;

  protected DaoClaimPayload() {}
}
