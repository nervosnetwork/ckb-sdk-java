package org.nervos.mercury.model.req.payload;

import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.item.Item;

import java.math.BigInteger;
import java.util.Set;

public class GetBalancePayload {
  public Item item;
  public Set<AssetInfo> assetInfos;
  public BigInteger tipBlockNumber;

  protected GetBalancePayload() {
  }
}
