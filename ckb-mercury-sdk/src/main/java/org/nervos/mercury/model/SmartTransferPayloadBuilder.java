package org.nervos.mercury.model;

import java.math.BigInteger;
import java.util.ArrayList;
import org.nervos.mercury.FeeConstant;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.ToInfo;
import org.nervos.mercury.model.req.payload.SmartTransferPayload;
import org.nervos.mercury.model.req.since.SinceConfig;

public class SmartTransferPayloadBuilder extends SmartTransferPayload {
  public SmartTransferPayloadBuilder() {
    this.feeRate = FeeConstant.DEFAULT_FEE_RATE;
    this.from = new ArrayList<>(1);
    this.to = new ArrayList<>(1);
  }

  public void assetInfo(AssetInfo assetInfo) {
    this.assetInfo = assetInfo;
  }

  public void addFrom(String address) {
    this.from.add(address);
  }

  public void addTo(ToInfo to) {
    this.to.add(to);
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

  public void payFee(String address) {
    this.payFee = address;
  }

  public SmartTransferPayload build() {
    return this;
  }
}
