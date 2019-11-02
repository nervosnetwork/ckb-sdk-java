package org.nervos.ckb.transaction;

import java.util.List;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class WitnessGroup {

  public List<Integer> indexes;
  public String privateKey;

  public WitnessGroup(List<Integer> indexes, String privateKey) {
    this.indexes = indexes;
    this.privateKey = privateKey;
  }
}
