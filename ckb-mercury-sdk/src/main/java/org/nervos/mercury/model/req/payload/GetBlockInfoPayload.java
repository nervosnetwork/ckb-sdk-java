package org.nervos.mercury.model.req.payload;

public class GetBlockInfoPayload {
  public int blockNumber;
  public byte[] blockHash;

  protected GetBlockInfoPayload() {
  }
}
