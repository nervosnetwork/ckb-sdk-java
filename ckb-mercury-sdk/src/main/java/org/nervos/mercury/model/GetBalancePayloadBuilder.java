package org.nervos.mercury.model;

import java.math.BigInteger;
import java.util.HashSet;

import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.payload.GetBalancePayload;

/**
 * @author zjh @Created Date: 2021/7/16 @Description: @Modify by:
 */
public class GetBalancePayloadBuilder extends GetBalancePayload {

  public GetBalancePayloadBuilder() {
    this.assetInfos = new HashSet<>(2, 1);
  }

  public void item(Item item) {
    this.item = item;
  }

  public void addAssetInfo(AssetInfo info) {
    this.assetInfos.add(info);
  }

  public void tipBlockNumber(BigInteger blockNum) {
    this.tipBlockNumber = blockNum;
  }

  public GetBalancePayload build() {
    return this;
  }
}
