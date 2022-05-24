package org.nervos.mercury.model.resp;

import org.nervos.mercury.model.common.Record;

import java.util.List;

public class TransactionInfoResponse {
  public byte[] txHash;
  public List<Record> records;
  public long fee;
  public List<BurnInfo> burn;
  public long timestamp;
}
