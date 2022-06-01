package org.nervos.mercury.model.req.payload;

import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.item.Item;

import java.util.List;

public class AdjustAccountPayload {
  public Item item;
  public List<Item> from;
  public AssetInfo assetInfo;
  public Integer accountNumber;
  public Long extraCkb;
  public Long feeRate;

  protected AdjustAccountPayload() {
  }
}
