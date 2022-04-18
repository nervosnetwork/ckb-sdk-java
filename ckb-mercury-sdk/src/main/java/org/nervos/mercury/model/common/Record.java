package org.nervos.mercury.model.common;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

import org.nervos.ckb.type.OutPoint;
import org.nervos.mercury.model.resp.Ownership;

public class Record {

  @SerializedName("out_point")
  public OutPoint outPoint;

  @SerializedName("ownership")
  public Ownership ownership;

  public BigInteger amount;

  public BigInteger occupied;

  @SerializedName("asset_info")
  public AssetInfo assetInfo;

  public RecordStatus status;

  public ExtraFilter extra;

  @SerializedName("block_number")
  public int blockNumber;

  @SerializedName("epoch_number")
  public BigInteger epochNumber;
}
