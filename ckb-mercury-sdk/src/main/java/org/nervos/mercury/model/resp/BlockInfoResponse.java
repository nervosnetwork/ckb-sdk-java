package org.nervos.mercury.model.resp;

import java.util.List;

/**
 * @author zjh @Created Date: 2021/7/20 @Description: @Modify by:
 */
public class BlockInfoResponse {
  public int blockNumber;
  public byte[] blockHash;
  public byte[] parentHash;
  public long timestamp;

  public List<TransactionInfoResponse> transactions;
}
