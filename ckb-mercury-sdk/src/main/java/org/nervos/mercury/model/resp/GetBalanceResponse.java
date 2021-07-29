package org.nervos.mercury.model.resp;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.List;

public class GetBalanceResponse {

  @SerializedName("block_num")
  public BigInteger blockNum;

  public List<BalanceResponse> balances;
}
