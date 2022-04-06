package org.nervos.mercury.model.resp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GetBalanceResponse {
  public List<BalanceResponse> balances;

  @SerializedName("tip_block_number")
  public int tipBlockNumber;
}
