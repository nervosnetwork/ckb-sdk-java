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
    this.to = new ArrayList<>();
    this.from = new ArrayList<>();
  }

  public void owner(String owner) {
    this.owner = owner;
  }

  public void addTo(String address, BigInteger amount) {
    this.to.add(new ToInfo(address, amount));
  }

  public void addFrom(Item from) {
    this.from.add(from);
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
