package org.nervos.mercury.model;

import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.common.ExtraFilter;
import org.nervos.mercury.model.common.PaginationRequest;
import org.nervos.mercury.model.common.Range;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.payload.QueryTransactionsPayload;

import java.util.HashSet;

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

  public void cursor(byte[] cursor) {
    this.pagination.cursor = cursor;
  }

  public void order(PaginationRequest.Order order) {
    this.pagination.order = order;
  }

  public void returnCount(Boolean returnCount) {
    this.pagination.returnCount = returnCount;
  }

  public void extra(ExtraFilter.Type extra) {
    this.extra = extra;
  }

  public QueryTransactionsPayload build() {
    return this;
  }
}
