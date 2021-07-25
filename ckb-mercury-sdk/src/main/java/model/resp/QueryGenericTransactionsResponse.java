package model.resp;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.List;

/** @author zjh @Created Date: 2021/7/26 @Description: @Modify by: */
public class QueryGenericTransactionsResponse {

  public List<GenericTransactionResponse> txs;

  @SerializedName("total_count")
  public BigInteger totalCount;

  @SerializedName("next_offset")
  public BigInteger nextOffset;
}
