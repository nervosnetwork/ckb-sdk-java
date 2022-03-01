package org.nervos.ckb.sign;

import java.util.HashSet;

public class Contexts extends HashSet<Context> {
  public void addPrivateKey(String ECPrivateKey) {
    this.add(new Context(ECPrivateKey));
  }

  public void addPrivateKeys(String... ECPrivateKeys) {
    for (String privateKey : ECPrivateKeys) {
      this.add(new Context(privateKey));
    }
  }
}
