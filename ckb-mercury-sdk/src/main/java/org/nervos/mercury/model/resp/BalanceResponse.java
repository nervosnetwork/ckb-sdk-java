package org.nervos.mercury.model.resp;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import org.nervos.mercury.model.common.AssetInfo;

/** @author zjh @Created Date: 2021/7/16 @Description: @Modify by: */
public class BalanceResponse {
  @SerializedName("ownership")
  public Ownership ownership;

  @SerializedName("asset_info")
  public AssetInfo assetInfo;

  public BigInteger occupied;

  public BigInteger free;

  public BigInteger claimable;

  public BigInteger freezed;
}
