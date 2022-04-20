package org.nervos.mercury.model.resp;

import org.nervos.mercury.model.common.Record;

import java.math.BigInteger;
import java.util.List;

/**
 * @author zjh @Created Date: 2021/7/20 @Description: @Modify by:
 */
public class TransactionInfoResponse {
  public byte[] txHash;
  public List<Record> records;
  public BigInteger fee;
  public List<BurnInfo> burn;
  public long timestamp;
}
