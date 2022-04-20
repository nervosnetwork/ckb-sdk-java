package org.nervos.mercury.model.req.payload;

import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.From;
import org.nervos.mercury.model.req.To;
import org.nervos.mercury.model.req.since.SinceConfig;

import java.math.BigInteger;

public class TransferPayload {
  public AssetInfo assetInfo;
  public From from;
  public To to;
  public String payFee;
  public String change;
  public BigInteger feeRate = new BigInteger("1000");
  public SinceConfig since;

  protected TransferPayload() {
  }
}
