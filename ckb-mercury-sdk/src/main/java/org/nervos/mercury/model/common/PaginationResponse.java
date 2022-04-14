package org.nervos.mercury.model.common;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.List;

/**
 * @author zjh @Created Date: 2021/7/26 @Description: @Modify by:
 */
public class PaginationResponse<T> {

  public List<T> response;

  @SerializedName("count")
  public BigInteger count;

  @SerializedName("next_cursor")
  public List<Integer> nextCursor;
}
