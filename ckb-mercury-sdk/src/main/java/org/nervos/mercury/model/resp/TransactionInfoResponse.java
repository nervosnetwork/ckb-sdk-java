package org.nervos.mercury.model.resp;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.List;
import org.nervos.mercury.model.common.Record;

/** @author zjh @Created Date: 2021/7/20 @Description: @Modify by: */
public class TransactionInfoResponse {
  @SerializedName("tx_hash")
  public byte[] txHash;

  public List<Record> records;

  public BigInteger fee;

  public List<BurnInfo> burn;

  public long timestamp;
}
