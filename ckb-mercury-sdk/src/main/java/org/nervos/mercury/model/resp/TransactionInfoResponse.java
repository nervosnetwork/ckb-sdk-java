package org.nervos.mercury.model.resp;

import org.nervos.mercury.model.common.Record;

import java.math.BigInteger;
import java.util.List;

public class TransactionInfoResponse {
  public byte[] txHash;
  public List<Record> records;
  public BigInteger fee;
  public List<BurnInfo> burn;
  public long timestamp;
}
