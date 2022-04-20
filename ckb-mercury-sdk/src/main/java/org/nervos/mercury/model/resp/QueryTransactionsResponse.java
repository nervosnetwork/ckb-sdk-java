package org.nervos.mercury.model.resp;

import java.math.BigInteger;
import java.util.List;

/**
 * @author zjh @Created Date: 2021/7/26 @Description: @Modify by:
 */
public class QueryTransactionsResponse {
  public List<TransactionInfoResponse> txs;
  public BigInteger totalCount;
  public BigInteger nextOffset;
}
