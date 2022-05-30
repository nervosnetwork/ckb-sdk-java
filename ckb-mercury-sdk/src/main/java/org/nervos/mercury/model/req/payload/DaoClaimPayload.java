package org.nervos.mercury.model.req.payload;

import org.nervos.mercury.model.req.item.Item;

public class DaoClaimPayload {
  public Item from;
  public String to;
  public Long feeRate;

  protected DaoClaimPayload() {
  }
}
