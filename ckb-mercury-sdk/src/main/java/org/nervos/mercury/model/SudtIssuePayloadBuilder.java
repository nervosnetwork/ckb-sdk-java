package org.nervos.mercury.model;

import org.nervos.mercury.FeeConstant;
import org.nervos.mercury.model.req.ToInfo;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.payload.CapacityProvider;
import org.nervos.mercury.model.req.payload.SudtIssuePayload;
import org.nervos.mercury.model.req.since.SinceConfig;

import java.math.BigInteger;
import java.util.ArrayList;

public class SudtIssuePayloadBuilder extends SudtIssuePayload {

  public SudtIssuePayloadBuilder() {
    this.feeRate = FeeConstant.DEFAULT_FEE_RATE;
  }

  public void owner(String owner) {
    this.owner = owner;
  }

  public void addTo(String address, BigInteger amount) {
    if (this.to == null) {
      this.to = new ArrayList<>();
    }
    this.to.add(new ToInfo(address, amount));
  }

  public void payFee(Item item) {
    this.payFee = item;
  }

  public void feeRate(Long feeRate) {
    this.feeRate = feeRate;
  }

  public void outputCapacityProvider(CapacityProvider outputCapacityProvider) {
    this.outputCapacityProvider = outputCapacityProvider;
  }

  public void since(SinceConfig since) {
    this.since = since;
  }

  public SudtIssuePayload build() {
    return this;
  }
}
