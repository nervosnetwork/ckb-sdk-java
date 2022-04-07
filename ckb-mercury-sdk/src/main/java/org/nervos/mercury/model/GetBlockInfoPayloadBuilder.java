package org.nervos.mercury.model;

import org.nervos.mercury.model.req.payload.GetBlockInfoPayload;

/** @author zjh @Created Date: 2021/7/20 @Description: @Modify by: */
public class GetBlockInfoPayloadBuilder extends GetBlockInfoPayload {

  public void blockNumber(int blockNumber) {
    this.blockNumber = blockNumber;
  }

  public void blockHash(byte[] blockHash) {
    this.blockHash = blockHash;
  }

  public GetBlockInfoPayload build() {
    return this;
  }
}
