package org.nervos.mercury.model.req.payload;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import org.nervos.mercury.model.req.To;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.since.SinceConfig;

public class SudtIssuePayload {

  public String owner;

  public To to;

  @SerializedName("pay_fee")
  public Item payFee;

  public String change;

  @SerializedName("fee_rate")
  public BigInteger feeRate = new BigInteger("1000");

  public SinceConfig since;

  protected SudtIssuePayload() {}
}
