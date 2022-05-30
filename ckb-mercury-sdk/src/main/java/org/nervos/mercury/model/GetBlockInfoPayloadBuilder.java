package org.nervos.mercury.model;

import org.nervos.mercury.model.req.payload.GetBlockInfoPayload;

public class GetBlockInfoPayloadBuilder extends GetBlockInfoPayload {

  public void blockNumber(Long blockNumber) {
    this.blockNumber = blockNumber;
  }

  public void blockHash(byte[] blockHash) {
    this.blockHash = blockHash;
  }

  public GetBlockInfoPayload build() {
    return this;
  }
}
