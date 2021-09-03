package org.nervos.mercury.model.req;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.List;
import org.nervos.mercury.model.resp.AssetInfo;

public class SmartTransferPayload {
  public AssetInfo assetInfo;
  public List<String> from;
  public List<SmartTo> to;
  public String change;

  @SerializedName("fee_rate")
  public BigInteger feeRate;

  protected SmartTransferPayload() {}
}
