package org.nervos.mercury.model.resp;

import java.util.List;

public class GetBalanceResponse {
  public List<BalanceResponse> balances;

  public static class RpcGetBalanceResponse {
    public List<BalanceResponse.RpcBalanceResponse> balances;
  }
}
