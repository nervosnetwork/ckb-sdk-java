package org.nervos.mercury.model.req.payload;

import org.nervos.mercury.model.req.To;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.since.SinceConfig;

import java.math.BigInteger;

public class SudtIssuePayload {
  public String owner;
  public To to;
  public Item payFee;
  public String change;
  public BigInteger feeRate = new BigInteger("1000");
  public SinceConfig since;

  protected SudtIssuePayload() {
  }
}
