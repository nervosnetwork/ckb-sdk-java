package org.nervos.mercury.model.common;

import java.math.BigInteger;
import java.util.List;

public class PaginationResponse<T> {
  public List<T> response;
  public BigInteger count;
  public List<Integer> nextCursor;
}
