package org.nervos.ckb.type;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class WitnessArgs {

  public String lock;
  public String inputType;
  public String outputType;

  public WitnessArgs() {
    this.lock =
        "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
  }
}
