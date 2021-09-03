package org.nervos.mercury.model;

import static java.util.stream.Collectors.toSet;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.nervos.mercury.FeeConstant;
import org.nervos.mercury.model.req.AdjustAccountPayload;
import org.nervos.mercury.model.resp.AssetInfo;

public class AdjustAccountPayloadBuilder extends AdjustAccountPayload {

  private List<AssetInfo> assetInfos;

  public AdjustAccountPayloadBuilder() {
    this.feeRate = FeeConstant.DEFAULT_FEE_RATE;
    this.assetInfos = new ArrayList<>(1);
  }

  public void address(String address) {
    this.address = address;
  }

  public void addAssetInfo(AssetInfo assetInfo) {
    this.assetInfos.add(assetInfo);
  }

  public void feeRate(BigInteger feeRate) {
    this.feeRate = feeRate;
  }

  public AdjustAccountPayload build() {
    this.udtHashes = this.assetInfos.stream().map(x -> x.udtHash).collect(toSet());

    return this;
  }
}
