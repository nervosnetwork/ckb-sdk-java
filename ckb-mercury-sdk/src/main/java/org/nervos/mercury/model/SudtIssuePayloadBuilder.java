package org.nervos.mercury.model;

import java.math.BigInteger;
import org.nervos.mercury.FeeConstant;
import org.nervos.mercury.model.req.To;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.payload.SudtIssuePayload;
import org.nervos.mercury.model.req.since.SinceConfig;

public class SudtIssuePayloadBuilder extends SudtIssuePayload {

  public SudtIssuePayloadBuilder() {
    this.feeRate = FeeConstant.DEFAULT_FEE_RATE;
  }

  public void owner(String owner) {
    this.owner = owner;
  }

  public void to(To to) {
    this.to = to;
  }

  public void payFee(Item item) {
    this.payFee = item;
  }

  public void change(String address) {
    this.change = address;
  }

  public void feeRate(BigInteger feeRate) {
    this.feeRate = feeRate;
  }

  public void since(SinceConfig since) {
    this.since = since;
  }

  public SudtIssuePayload build() {
    assert !(this.to == null) : "items not null";

    return this;
  }
}
