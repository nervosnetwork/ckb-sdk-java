package org.nervos.ckb.type.cell;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CellWithStatus {

  public CellInfo cell;
  public String status;

  public static class CellInfo {
    public CellData data;
    public CellOutput output;

    public static class CellData {
      public String content;
      public String hash;
    }
  }

  public enum Status {
    LIVE("live"),
    DEAD("dead"),
    UNKNOWN("unknown");

    private final String value;

    Status(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }
}
