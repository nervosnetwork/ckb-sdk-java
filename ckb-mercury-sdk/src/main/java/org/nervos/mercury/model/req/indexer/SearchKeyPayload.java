package org.nervos.mercury.model.req.indexer;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import org.nervos.indexer.SearchKey;

public class SearchKeyPayload {
  @SerializedName("search_key")
  public SearchKey searchKey;

  public String order;

  public BigInteger limit;

  @SerializedName("after_cursor")
  public String afterCursor;

  protected SearchKeyPayload() {}
}
