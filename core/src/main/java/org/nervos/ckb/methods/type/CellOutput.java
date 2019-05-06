package org.nervos.ckb.methods.type;

/** Created by duanyytop on 2019-01-08. Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CellOutput {

  /**
   * capacity : 50000 type : null data : 0x lock :
   * 0x321c1ca2887fb8eddaaa7e917399f71e63e03a1c83ff75ed12099a01115ea2ff
   */
  public String capacity;

  public Script type;
  public Script lock;
  public String data;

  public CellOutput() {}

  public CellOutput(String capacity, String data, Script lock) {
    this.capacity = capacity;
    this.data = data;
    this.lock = lock;
  }
}
