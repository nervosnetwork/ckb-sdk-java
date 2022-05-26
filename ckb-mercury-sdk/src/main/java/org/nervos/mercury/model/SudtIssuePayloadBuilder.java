package org.nervos.mercury.model;

import org.nervos.mercury.FeeConstant;
import org.nervos.mercury.model.req.ToInfo;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.payload.CapacityProvider;
import org.nervos.mercury.model.req.payload.SudtIssuePayload;
import org.nervos.mercury.model.req.since.SinceConfig;

import java.util.ArrayList;

public class SudtIssuePayloadBuilder extends SudtIssuePayload {

  public SudtIssuePayloadBuilder() {
    this.feeRate = FeeConstant.DEFAULT_FEE_RATE;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public void addTo(String address, long amount) {
    if (this.to == null) {
      this.to = new ArrayList<>();
    }
    this.to.add(new ToInfo(address, amount));
  }

  public void setPayFee(Item item) {
    this.payFee = item;
  }

  public void setFeeRate(Long feeRate) {
    this.feeRate = feeRate;
  }

  public void setOutputCapacityProvider(CapacityProvider outputCapacityProvider) {
    this.outputCapacityProvider = outputCapacityProvider;
  }

  public void setSince(SinceConfig since) {
    this.since = since;
  }

  public SudtIssuePayload build() {
    return this;
  }
}
