package org.nervos.ckb.methods.type;

import java.util.List;

public class Witness {
  public List<String> data;

  public Witness(List<String> data) {
    this.data = data;
  }
}
