package org.nervos.mercury.model.req.payload;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.Set;

import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.item.Item;

public class AdjustAccountPayload {

  public Item item;

  public Set<Item> from;

  @SerializedName("asset_info")
  public AssetInfo assetInfo;

  @SerializedName("account_number")
  public BigInteger accountNumber;

  @SerializedName("extra_ckb")
  public long extraCkb;

  @SerializedName("fee_rate")
  public BigInteger feeRate;

  protected AdjustAccountPayload() {
  }
}
