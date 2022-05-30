package org.nervos.mercury.model.resp;

import java.util.List;

public class GetBalanceResponse {
  public List<BalanceResponse> balances;
  public long tipBlockNumber;
}
