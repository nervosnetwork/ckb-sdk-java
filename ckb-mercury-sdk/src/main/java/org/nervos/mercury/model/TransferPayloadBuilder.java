package org.nervos.mercury.model;

import org.nervos.mercury.FeeConstant;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.ToInfo;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.payload.CapacityProvider;
import org.nervos.mercury.model.req.payload.TransferPayload;
import org.nervos.mercury.model.req.since.SinceConfig;

import java.math.BigInteger;
import java.util.ArrayList;

public class TransferPayloadBuilder extends TransferPayload {

  public TransferPayloadBuilder() {
    this.assetInfo = AssetInfo.newCkbAsset();
    this.feeRate = FeeConstant.DEFAULT_FEE_RATE;
    this.from = new ArrayList<>();
    this.to = new ArrayList<>();
  }

  public void addFrom(Item from) {
    this.from.add(from);
  }

  public void assetInfo(AssetInfo assetInfo) {
    this.assetInfo = assetInfo;
  }

  public void addTo(String address, BigInteger amount) {
    ToInfo toInfo = new ToInfo(address, amount);
    this.to.add(toInfo);
  }

  public void addTo(String address, long amount) {
    addTo(address, BigInteger.valueOf(amount));
  }

  public void outputCapacityProvider(CapacityProvider outputCapacityProvider) {
    this.outputCapacityProvider = outputCapacityProvider;
  }

  public void payFee(PayFee payFee) {
    this.payFee = payFee;
  }

  public void feeRate(Long feeRate) {
    this.feeRate = feeRate;
  }

  public void since(SinceConfig since) {
    this.since = since;
  }

  public TransferPayload build() {
    return this;
  }
}
