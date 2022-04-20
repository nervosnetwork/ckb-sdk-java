package org.nervos.mercury.model.common;

import org.nervos.ckb.type.OutPoint;
import org.nervos.mercury.model.resp.Ownership;

import java.math.BigInteger;

public class Record {
  public OutPoint outPoint;
  public Ownership ownership;
  public BigInteger amount;
  public BigInteger occupied;
  public AssetInfo assetInfo;
  public RecordStatus status;
  public ExtraFilter extra;
  public int blockNumber;
  public BigInteger epochNumber;
}
