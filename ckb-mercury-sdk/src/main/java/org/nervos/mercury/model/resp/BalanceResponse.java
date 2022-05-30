package org.nervos.mercury.model.resp;

import org.nervos.mercury.model.common.AssetInfo;

import java.math.BigInteger;

public class BalanceResponse {
  public String ownership;
  public AssetInfo assetInfo;
  public BigInteger occupied;
  public BigInteger free;
  public BigInteger frozen;
}
