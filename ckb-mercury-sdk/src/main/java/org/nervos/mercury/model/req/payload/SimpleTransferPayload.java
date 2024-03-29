package org.nervos.mercury.model.req.payload;

import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.ToInfo;
import org.nervos.mercury.model.req.since.SinceConfig;

import java.util.List;

public class SimpleTransferPayload {
  public AssetInfo assetInfo;
  public List<String> from;
  public List<ToInfo> to;
  public Long feeRate;
  public SinceConfig since;
}
