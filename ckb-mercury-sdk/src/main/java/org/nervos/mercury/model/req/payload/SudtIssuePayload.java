package org.nervos.mercury.model.req.payload;

import org.nervos.mercury.model.req.To;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.since.SinceConfig;

public class SudtIssuePayload {
  public String owner;
  public To to;
  public Item payFee;
  public String change;
  public Long feeRate = 1000L;
  public SinceConfig since;

  protected SudtIssuePayload() {
  }
}
