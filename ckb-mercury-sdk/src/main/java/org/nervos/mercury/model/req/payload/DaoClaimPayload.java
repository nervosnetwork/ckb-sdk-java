package org.nervos.mercury.model.req.payload;

import org.nervos.mercury.model.req.item.Item;

import java.util.List;

public class DaoClaimPayload {
  public List<Item> from;
  public String to;
  public Long feeRate;

  protected DaoClaimPayload() {
  }
}
