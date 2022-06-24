package org.nervos.mercury.model;

import org.nervos.mercury.model.common.*;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.payload.QueryTransactionsPayload;

import java.util.HashSet;

public class QueryTransactionsPayloadBuilder extends QueryTransactionsPayload {

  public QueryTransactionsPayloadBuilder() {
    this.assetInfos = new HashSet<>(2, 1);
    this.pagination = new PaginationRequest();
    this.pagination.limit = 50L;
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

  public void limit(Long limit) {
    this.pagination.limit = limit;
  }

  public void cursor(Long cursor) {
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

  public void structureType(StructureType structureType) {
    this.structureType = structureType;
  }

  public QueryTransactionsPayload build() {
    return this;
  }
}
