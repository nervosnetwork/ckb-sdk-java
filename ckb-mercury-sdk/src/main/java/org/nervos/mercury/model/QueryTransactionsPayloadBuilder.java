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
    this.pagination.limit = new BigInteger("50");
    this.pagination.order = PaginationRequest.ORDER_BY_DESC;
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

  public void limit(BigInteger limit) {
    this.pagination.limit = limit;
  }

  public void cursor(List<Integer> cursor) {
    this.pagination.cursor = cursor;
  }

  public void order(String order) {
    this.pagination.order = order;
  }

  public void pageNumber(BigInteger skip) {
    this.pagination.skip = skip;
  }

  public void returnCount(Boolean returnCount) {
    this.pagination.returnCount = returnCount;
  }

  public void extraFilter(ExtraFilterType extraFilterType) {
    this.extraFilterType = extraFilterType;
  }

  public QueryTransactionsPayload build() {
    if (Objects.nonNull(this.pagination.skip)) {
      this.pagination.skip =
          this.pagination.limit.multiply(this.pagination.skip).subtract(this.pagination.limit);
    }

    if (Objects.isNull(this.pagination.cursor) && Objects.equals(this.pagination.order, "desc")) {
      pagination.cursor = Arrays.asList(127, 255, 255, 255, 255, 255, 255, 254);
    }

    return this;
  }
}
