package org.nervos.mercury.model.common;

import java.math.BigInteger;
import java.util.List;

/**
 * @author zjh @Created Date: 2021/7/26 @Description: @Modify by:
 */
public class PaginationResponse<T> {
  public List<T> response;
  public BigInteger count;
  public List<Integer> nextCursor;
}
