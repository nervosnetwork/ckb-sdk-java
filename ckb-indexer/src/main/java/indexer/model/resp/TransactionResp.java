package indexer.model.resp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionResp {

  @SerializedName("last_cursor")
  public String lastCursor;

  public List<TransactionInfo> objects;
}
