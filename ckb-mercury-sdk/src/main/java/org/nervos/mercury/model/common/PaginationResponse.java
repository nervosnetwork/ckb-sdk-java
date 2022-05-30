package org.nervos.mercury.model.common;

import org.nervos.mercury.model.resp.TxView;

import java.util.List;

public class PaginationResponse<T> {
  public List<TxView<T>> response;
  public Long count;
  public Long nextCursor;
}
