package org.nervos.mercury.model.req.payload;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.Set;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.item.Item;

public class GetBalancePayload {
  public Item item;

  @SerializedName("asset_infos")
  public Set<AssetInfo> assetInfos;

  @SerializedName("tip_block_number")
  public BigInteger tipBlockNumber;

  protected GetBalancePayload() {}
}
