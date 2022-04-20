package org.nervos.mercury.model.req.payload;

import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.item.Item;

import java.math.BigInteger;
import java.util.Set;

public class AdjustAccountPayload {
  public Item item;
  public Set<Item> from;
  public AssetInfo assetInfo;
  public BigInteger accountNumber;
  public long extraCkb;
  public BigInteger feeRate;

  protected AdjustAccountPayload() {
  }
}
