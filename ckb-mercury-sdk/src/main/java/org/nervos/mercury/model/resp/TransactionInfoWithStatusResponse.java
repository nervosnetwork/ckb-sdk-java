package org.nervos.mercury.model.resp;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import org.nervos.ckb.type.transaction.TransactionWithStatus;

/** @author zjh @Created Date: 2021/7/20 @Description: @Modify by: */
public class TransactionInfoWithStatusResponse {

  public TransactionInfoResponse transaction;

  public TransactionWithStatus.Status status;

  @SerializedName("block_hash")
  public String blockHash;

  @SerializedName("block_number")
  public BigInteger blockNumber;

  @SerializedName("confirmed_number")
  public BigInteger confirmedNumber;
}
