package org.nervos.mercury.model.req.payload;

import org.nervos.mercury.model.common.*;
import org.nervos.mercury.model.req.item.Item;

import java.util.Set;

/**
 * @author zjh @Created Date: 2021/7/26 @Description: @Modify by:
 */
public class QueryTransactionsPayload {
  public Item item;
  public Set<AssetInfo> assetInfos;
  public ExtraFilterType extraFilterType;
  public Range blockRange;
  public PaginationRequest pagination;
  public ViewType viewType;

  protected QueryTransactionsPayload() {
  }
}
