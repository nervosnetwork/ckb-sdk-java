package org.nervos.ckb.methods.type;

/** Created by duanyytop on 2019-01-08. Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CellOutput {
  public long capacity;

  public Script type;
  public Script lock;
  public String data;

  public CellOutput() {}

  public CellOutput(long capacity, String data, Script lock) {
    this.capacity = capacity;
    this.data = data;
    this.lock = lock;
  }
}
