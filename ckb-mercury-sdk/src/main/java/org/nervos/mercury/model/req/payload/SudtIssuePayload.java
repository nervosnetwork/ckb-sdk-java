package org.nervos.mercury.model.req.payload;

import org.nervos.mercury.model.req.ToInfo;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.since.SinceConfig;

import java.util.List;

public class SudtIssuePayload {
  public String owner;
  public List<Item> from;
  public List<ToInfo> to;
  public CapacityProvider outputCapacityProvider;
  public Long feeRate = 1000L;
  public SinceConfig since;

  protected SudtIssuePayload() {
  }
}
