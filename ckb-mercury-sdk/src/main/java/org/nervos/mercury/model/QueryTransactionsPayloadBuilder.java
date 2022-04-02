package org.nervos.mercury.model;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.common.ExtraFilterType;
import org.nervos.mercury.model.common.PaginationRequest;
import org.nervos.mercury.model.common.Range;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.payload.QueryTransactionsPayload;

/** @author zjh @Created Date: 2021/7/26 @Description: @Modify by: */
public class QueryTransactionsPayloadBuilder extends QueryTransactionsPayload {

  public QueryTransactionsPayloadBuilder() {
    this.assetInfos = new HashSet<>(2, 1);
    this.pagination = new PaginationRequest();
    this.pagination.limit = 50;
    this.pagination.order = PaginationRequest.Order.DESC;
    this.pagination.returnCount = Boolean.FALSE;
  }

  public void item(Item item) {
    this.item = item;
  }

  public void addAssetInfo(AssetInfo info) {
    this.assetInfos.add(info);
  }

  public void range(Range range) {
    this.blockRange = range;
  }

  public void limit(int limit) {
    this.pagination.limit = limit;
  }

  public void cursor(List<Integer> cursor) {
    this.pagination.cursor = cursor;
  }

  public void order(PaginationRequest.Order order) {
    this.pagination.order = order;
  }


  public void returnCount(Boolean returnCount) {
    this.pagination.returnCount = returnCount;
  }

  public void extraFilter(ExtraFilterType extraFilterType) {
    this.extraFilterType = extraFilterType;
  }

  public QueryTransactionsPayload build() {
    if (Objects.isNull(this.pagination.cursor) && Objects.equals(this.pagination.order, "desc")) {
      pagination.cursor = Arrays.asList(127, 255, 255, 255, 255, 255, 255, 254);
    }

    return this;
  }
}
