package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Created by duanyytop on 2018-12-21. Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CellOutputWithOutPoint {
  public long capacity;

  public String lock;

  @JsonProperty("out_point")
  public OutPoint outPoint;
}
