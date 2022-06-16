package org.nervos.mercury.model.req.payload;

import org.nervos.mercury.model.req.item.Item;

import java.util.List;

public class DaoDepositPayload {
  public List<Item> from;
  public String to;
  public long amount;
  public Long feeRate;
}
