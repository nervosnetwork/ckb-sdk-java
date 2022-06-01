package org.nervos.mercury.model.req.payload;

import org.nervos.mercury.model.req.item.Item;

import java.util.List;

public class DaoWithdrawPayload {
  public List<Item> from;
  public Long feeRate;

  protected DaoWithdrawPayload() {
  }
}
