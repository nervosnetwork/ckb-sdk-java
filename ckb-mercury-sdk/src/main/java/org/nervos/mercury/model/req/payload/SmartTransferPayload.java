package org.nervos.mercury.model.req.payload;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.List;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.ToInfo;
import org.nervos.mercury.model.req.since.SinceConfig;

public class SmartTransferPayload {

  @SerializedName("asset_info")
  public AssetInfo assetInfo;

  public List<String> from;
  public List<ToInfo> to;

  @SerializedName("pay_Fee")
  public String payFee;

  public String change;

  @SerializedName("fee_rate")
  public BigInteger feeRate;

  public SinceConfig since;

  protected SmartTransferPayload() {}
}
