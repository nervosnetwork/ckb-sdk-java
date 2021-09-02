package org.nervos.mercury.model;

import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.nervos.mercury.model.req.GetBalancePayload;
import org.nervos.mercury.model.req.KeyAddress;
import org.nervos.mercury.model.resp.AssetInfo;

/** @author zjh @Created Date: 2021/7/16 @Description: @Modify by: */
public class GetBalancePayloadBuilder extends GetBalancePayload {

  private List<AssetInfo> assetInfos;

  public GetBalancePayloadBuilder() {
    this.assetInfos = new ArrayList<>(1);
  }

  public void addAssetInfo(AssetInfo assetInfo) {
    this.assetInfos.add(assetInfo);
  }

  public void address(String address) {
    this.address = new KeyAddress(address);
  }

  public GetBalancePayload build() {
    if (this.assetInfos.size() == 0) {
      this.udtHashes = Collections.EMPTY_SET;
    }

    this.udtHashes = this.assetInfos.stream().map(x -> x.udtHash).collect(toSet());

    return this;
  }
}
