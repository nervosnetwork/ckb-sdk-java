package org.nervos.mercury.model;

import org.nervos.mercury.FeeConstant;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.ToInfo;
import org.nervos.mercury.model.req.payload.SimpleTransferPayload;
import org.nervos.mercury.model.req.since.SinceConfig;

import java.math.BigInteger;
import java.util.ArrayList;

public class SimpleTransferPayloadBuilder extends SimpleTransferPayload {
  public SimpleTransferPayloadBuilder() {
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

  public void addTo(String address, BigInteger amount) {
    ToInfo toInfo = new ToInfo(address, amount);
    this.to.add(toInfo);
  }

  public void addTo(String address, long amount) {
    addTo(address, BigInteger.valueOf(amount));
  }

  public void feeRate(Long feeRate) {
    this.feeRate = feeRate;
  }

  public void since(SinceConfig since) {
    this.since = since;
  }

  public SimpleTransferPayload build() {
    return this;
  }
}
