package org.nervos.mercury.model.req.payload;

public class GetBlockInfoPayload {
  public Long blockNumber;
  public byte[] blockHash;

  protected GetBlockInfoPayload() {
  }
}
