package org.nervos.mercury.model;

import org.nervos.mercury.FeeConstant;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.ToInfo;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.payload.TransferPayload;
import org.nervos.mercury.model.req.since.SinceConfig;

import java.util.ArrayList;

public class TransferPayloadBuilder extends TransferPayload {

  public TransferPayloadBuilder() {
    this.assetInfo = AssetInfo.newCkbAsset();
    this.feeRate = FeeConstant.DEFAULT_FEE_RATE;
  }

  public void addFrom(Item from) {
    if (this.from == null) {
      this.from = new ArrayList<>();
    }
    this.from.add(from);
  }

  public void setAssetInfo(AssetInfo assetInfo) {
    this.assetInfo = assetInfo;
  }

  public void addTo(String address, long amount) {
    if (this.to == null) {
      this.to = new ArrayList<>();
    }
    ToInfo toInfo = new ToInfo(address, amount);
    this.to.add(toInfo);
  }

  public void setPayFee(CapacityProvider payFee) {
    this.payFee = payFee;
  }

  public void setFeeRate(Long feeRate) {
    this.feeRate = feeRate;
  }

  public void setSince(SinceConfig since) {
    this.since = since;
  }

  public TransferPayload build() {
    assert !(this.from == null) : "from not null";
    assert !(this.to == null) : "items not null";

    return this;
  }
}
