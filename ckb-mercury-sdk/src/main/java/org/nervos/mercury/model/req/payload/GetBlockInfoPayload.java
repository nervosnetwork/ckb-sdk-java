package org.nervos.mercury.model.req.payload;

/**
 * @author zjh @Created Date: 2021/7/20 @Description: @Modify by:
 */
public class GetBlockInfoPayload {
  public int blockNumber;
  public byte[] blockHash;

  protected GetBlockInfoPayload() {
  }
}
