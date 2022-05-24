package org.nervos.mercury.model.req.payload;

import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.From;
import org.nervos.mercury.model.req.To;
import org.nervos.mercury.model.req.since.SinceConfig;

public class TransferPayload {
  public AssetInfo assetInfo;
  public From from;
  public To to;
  public String payFee;
  public String change;
  public Long feeRate = 1000L;
  public SinceConfig since;

  protected TransferPayload() {
  }
}
