package org.nervos.mercury.model.resp;

import org.nervos.mercury.model.common.AssetInfo;

public class BalanceResponse {
  public String ownership;
  public AssetInfo assetInfo;
  public long occupied;
  public long free;
  public long frozen;
}
