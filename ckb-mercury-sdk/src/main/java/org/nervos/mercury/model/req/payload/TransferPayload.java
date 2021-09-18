package org.nervos.mercury.model.req.payload;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.From;
import org.nervos.mercury.model.req.To;
import org.nervos.mercury.model.req.since.SinceConfig;

public class TransferPayload {

  @SerializedName("asset_info")
  public AssetInfo assetInfo;

  public From from;

  public To to;

  @SerializedName("pay_fee")
  public String payFee;

  public String change;

  @SerializedName("fee_rate")
  public BigInteger feeRate = new BigInteger("1000");

  public SinceConfig since;

  protected TransferPayload() {}
}
