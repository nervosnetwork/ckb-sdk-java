package org.nervos.ckb.type;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Witness {

  public static final String EMPTY_LOCK =
      "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

  public String lock;
  public String inputType;
  public String outputType;

  public Witness() {
    this.lock = "";
    this.inputType = "";
    this.outputType = "";
  }
}
