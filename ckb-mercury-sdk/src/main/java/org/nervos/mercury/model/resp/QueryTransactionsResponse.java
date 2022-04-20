package org.nervos.mercury.model.resp;

import java.math.BigInteger;
import java.util.List;

public class QueryTransactionsResponse {
  public List<TransactionInfoResponse> txs;
  public BigInteger totalCount;
  public BigInteger nextOffset;
}
