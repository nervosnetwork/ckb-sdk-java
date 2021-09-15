package org.nervos.mercury.model.resp;

import com.google.gson.annotations.SerializedName;
import org.nervos.mercury.model.common.AssetInfo;

import java.math.BigInteger;

/** @author zjh @Created Date: 2021/7/16 @Description: @Modify by: */
public class BalanceResponse {
  @SerializedName("address_or_lock_hash")
  public AddressOrLockHash addressOrLockHash;

  @SerializedName("asset_info")
  public AssetInfo assetInfo;

  public BigInteger occupied;

  public BigInteger free;

  public BigInteger claimable;

  public BigInteger freezed;
}
