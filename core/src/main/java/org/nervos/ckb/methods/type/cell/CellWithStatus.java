package org.nervos.ckb.methods.type.cell;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CellWithStatus {
  public CellInfo cell;
  public String status;

  public static class CellInfo {
    public CellData data;
    public CellOutput output;

    static class CellData {
      String content;
      String hash;
    }
  }
}
