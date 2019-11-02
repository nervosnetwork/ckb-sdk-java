package org.nervos.ckb.type.cell;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
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
