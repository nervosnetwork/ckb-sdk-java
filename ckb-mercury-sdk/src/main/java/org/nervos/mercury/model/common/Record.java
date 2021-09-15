package org.nervos.mercury.model.common;

import com.google.gson.annotations.SerializedName;
import org.nervos.mercury.model.resp.AddressOrLockHash;

import java.math.BigInteger;

public class Record {

  public String id;

  @SerializedName("address_or_lock_hash")
  public AddressOrLockHash addressOrLockHash;

  public BigInteger amount;

  public BigInteger occupied;

  @SerializedName("asset_info")
  public AssetInfo assetInfo;

  public RecordStatus status;

  public ExtraFilter extra;
}
