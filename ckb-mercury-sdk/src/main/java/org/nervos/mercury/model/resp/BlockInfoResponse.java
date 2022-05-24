package org.nervos.mercury.model.resp;

import java.util.List;

public class BlockInfoResponse {
  public long blockNumber;
  public byte[] blockHash;
  public byte[] parentHash;
  public long timestamp;
  public List<TransactionInfoResponse> transactions;
}
