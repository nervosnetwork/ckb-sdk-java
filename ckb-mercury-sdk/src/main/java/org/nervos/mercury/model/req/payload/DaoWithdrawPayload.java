package org.nervos.mercury.model.req.payload;

import org.nervos.mercury.model.req.item.Item;

public class DaoWithdrawPayload {
  public Item from;
  public String payFee;
  public Long feeRate;

  protected DaoWithdrawPayload() {
  }
}
