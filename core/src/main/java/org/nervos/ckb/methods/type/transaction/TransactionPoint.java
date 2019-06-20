package org.nervos.ckb.methods.type.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Created by duanyytop on 2019-06-20. Copyright © 2019 Nervos Foundation. All rights reserved. */
public class TransactionPoint {
  @JsonProperty("block_number")
  public String blockNumber;

  @JsonProperty("tx_hash")
  public String txHash;

  public String index;
}
