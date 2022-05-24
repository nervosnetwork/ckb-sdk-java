package org.nervos.mercury.model;

import org.nervos.mercury.FeeConstant;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.From;
import org.nervos.mercury.model.req.To;
import org.nervos.mercury.model.req.payload.TransferPayload;
import org.nervos.mercury.model.req.since.SinceConfig;

public class TransferPayloadBuilder extends TransferPayload {

  public TransferPayloadBuilder() {
    this.assetInfo = AssetInfo.newCkbAsset();
    this.feeRate = FeeConstant.DEFAULT_FEE_RATE;
  }

  public void from(From from) {
    this.from = from;
  }

  public void assetInfo(AssetInfo assetInfo) {
    this.assetInfo = assetInfo;
  }

  public void to(To to) {
    this.to = to;
  }

  public void payFee(String address) {
    this.payFee = address;
  }

  public void change(String address) {
    this.change = address;
  }

  public void feeRate(Long feeRate) {
    this.feeRate = feeRate;
  }

  public void since(SinceConfig since) {
    this.since = since;
  }

  public TransferPayload build() {
    assert !(this.from == null) : "from not null";
    assert !(this.to == null) : "items not null";

    return this;
  }
}
