package org.nervos.mercury.model.common;

import com.google.gson.annotations.SerializedName;
import org.nervos.ckb.type.OutPoint;

import java.math.BigInteger;

public class Record {
  public OutPoint outPoint;
  public String ownership;
  public IoType ioType;
  public BigInteger amount;
  public long occupied;
  public AssetInfo assetInfo;
  public ExtraFilter extra;
  public long blockNumber;
  public long epochNumber;

  enum IoType {
    @SerializedName("Input")
    INPUT,
    @SerializedName("Output")
    OUTPUT
  }
}
