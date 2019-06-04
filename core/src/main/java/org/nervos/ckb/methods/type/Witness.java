package org.nervos.ckb.methods.type;

import java.util.ArrayList;
import java.util.List;

public class Witness {
  public List<String> data = new ArrayList<>();

  public Witness() {}

  public Witness(List<String> data) {
    this.data = data;
  }
}
