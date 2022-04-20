package org.nervos.mercury.model.resp;

import org.nervos.mercury.model.common.AssetInfo;

import java.math.BigInteger;

/**
 * @author zjh @Created Date: 2021/7/16 @Description: @Modify by:
 */
public class BalanceResponse {
  public Ownership ownership;
  public AssetInfo assetInfo;
  public BigInteger occupied;
  public BigInteger free;
  public BigInteger claimable;
  public BigInteger frozen;
}
