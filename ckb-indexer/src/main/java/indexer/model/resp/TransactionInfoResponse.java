package indexer.model.resp;

import com.google.gson.annotations.SerializedName;

public class TransactionInfoResponse {
  @SerializedName("block_number")
  public String blockNumber;

  @SerializedName("io_index")
  public String ioIndex;

  @SerializedName("io_type")
  public String ioType;

  @SerializedName("tx_hash")
  public String txHash;

  @SerializedName("tx_index")
  public String txIndex;
}
