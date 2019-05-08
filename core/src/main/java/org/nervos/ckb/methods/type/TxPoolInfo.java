package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Created by duanyytop on 2019-05-08. Copyright © 2019 Nervos Foundation. All rights reserved. */
public class TxPoolInfo {

  public long pending;
  public long staging;
  public long orphan;

  @JsonProperty("last_txs_updated_at")
  public String lastTxsUpdatedAt;
}
