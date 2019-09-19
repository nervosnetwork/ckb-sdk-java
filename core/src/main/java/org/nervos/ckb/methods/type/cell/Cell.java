package org.nervos.ckb.methods.type.cell;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class Cell {
  public CellWithData cell;
  public String status;

  public static class CellWithData {
    public Data data;
    public CellOutput output;

    static class Data {
      String content;
      String hash;
    }
  }
}
