package model.resp;

import com.google.gson.annotations.SerializedName;

import org.nervos.ckb.type.transaction.TransactionWithStatus;

import java.math.BigInteger;

/** @author zjh @Created Date: 2021/7/20 @Description: @Modify by: */
public class GenericTransactionWithStatusResponse {

  public GenericTransactionResponse transaction;

  public TransactionWithStatus.Status status;

  @SerializedName("block_hash")
  public String blockHash;

  @SerializedName("block_number")
  public BigInteger blockNumber;

  @SerializedName("confirmed_number")
  public BigInteger confirmedNumber;
}
