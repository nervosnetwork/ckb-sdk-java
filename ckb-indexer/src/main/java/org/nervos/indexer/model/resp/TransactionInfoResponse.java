package org.nervos.indexer.model.resp;

import com.google.gson.annotations.SerializedName;

public class TransactionInfoResponse {
  public int blockNumber;
  public int ioIndex;
  public IoType ioType;
  public byte[] txHash;
  public int txIndex;

  public enum IoType {
    @SerializedName("input")
    INPUT,
    @SerializedName("output")
    OUTPUT;
  }
}
